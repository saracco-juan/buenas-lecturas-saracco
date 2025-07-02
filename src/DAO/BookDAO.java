package DAO;

import API.APIClient;
import Model.Book;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BookDAO {

    //Dependencia para el acceso a la API
    private final APIClient apiClient;
    //Dependecia que me va a permitir pasar los objetos JSON a objetos JAVA
    private final ObjectMapper objectMapper;
    //Dependencia que me va a permitir interactuar con los autores
    private final AuthorDAO authorDAO;

    public BookDAO() {
        //Al crear la clase, instancio el api client, el object mapper y el AuthorDAO
        this.apiClient = new APIClient(); // Crea la instancia
        this.objectMapper = new ObjectMapper();
        this.authorDAO = new AuthorDAO();
    }

    //Ambos metodos devuleven una promesa de una lista de libros que se va a completar de forma asincrona

    public CompletableFuture<List<Book>> searchByTitle(String title){
        //Metodo para buscar libro, recibe el titulo del libro por parametro

        //Construyo la URL codificando el títul. Esto hace mas amigables las busquedas por URL
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        //Con esta url + el titulo codificado armo lo que va a ser la peticion
        String url = "https://openlibrary.org/search.json?q=" + encodedTitle;

        //test
        //System.out.println("BookDAO llama a la API con la URL: " + url);

        //Utilizo el apiClient para hacer la peticion
       return apiClient.getAsync(url)
               .thenApply(jsonBody -> {

                   //Validacion por si falla la peticion
                   if(jsonBody == null){
                       //Si falla devuelvo una lista vacia
                       //System.out.println("ERROR: no se pudo realizar la peticion");
                       return Collections.emptyList();
                   }

                   //Parseo el String JSON a un objeto Java
                   try {
                       //Inicializo la lista que voy a querer retornar
                       List<Book> books = new ArrayList<>();

                       //Leo el JSON
                       JsonNode rootNode = objectMapper.readTree(jsonBody);
                        //"docs" es el array que contiene los resultados
                       JsonNode docsNode = rootNode.path("docs");

                       //Recorro ese docsNode
                       for (JsonNode bookNode : docsNode) {
                           //Por cada elemento en el array "docs", creo un objeto Book

                           //Extraigo los campos workID y BookTitle. Use .path() que no lanza error si el campo no existe
                           String workId = bookNode.path("key").asText();
                           String bookTitle = bookNode.path("title").asText("Titulo no disponible");

                           //El autor tambien es un array. Lo inicializo en desonocido por si no lo encuentro
                           String authorName = "Desconocido";

                           //Si encuentro un autor lo seteo en la variable authorName
                           if (bookNode.has("author_name") && bookNode.get("author_name").isArray() && bookNode.get("author_name").size() > 0) {
                               authorName = bookNode.get("author_name").get(0).asText();
                           }else{
                               //Test
                               //System.out.println("No se encontro el author");
                           }

                           //Creo la instancia del libro
                           Book book = new Book(workId, bookTitle, authorName);
                           //Lo añado a la lista que voy a retornar
                           books.add(book);
                       }
                       //Retorno la lista
                       return books;

                   } catch (Exception e) {
                       e.printStackTrace();
                       return Collections.<Book>emptyList();
                   }
               });
    }

    public CompletableFuture<Book> findByKey(String key) {
        //Metodo para buscar libro, recive la "Key" por parametro


        // La URL se construye a partir de la clave
        String url = "https://openlibrary.org" + key + ".json";

        //Utilizo el apiClient para hacer la peticion
        return apiClient.getAsync(url)
                //Then compose me permite encadenar otra peticion asincronica
                .thenCompose(jsonBody -> {
                    if (jsonBody == null) {
                        // Si la primera llamada falla, devuelvo null
                        //Test
                        System.out.println("ERROR: Error al realizar la peticion (.thenCompose)");
                        return CompletableFuture.completedFuture(null);
                    }
                    try {
                        //Pareseo el objeto Json
                        JsonNode bookNode = objectMapper.readTree(jsonBody);
                        //Extraigo el campo title, si es nulo le paso un valor por defecto
                        String title = bookNode.path("title").asText("Sin Título");

                        //Extraigo la key del autor del JSON del libro.
                        JsonNode authorNode = bookNode.path("authors").get(0).path("author").path("key");
                        //Guardo la key del autor en una variable
                        String authorKey = authorNode.asText();

                        // Si no hay autor, creamos el libro con autor desconocido
                        if (authorKey.isEmpty()) {
                            Book book = new Book(key, title, "Desconocido");
                            //Retorno la promesa completada con el objeto book que buscaba
                            return CompletableFuture.completedFuture(book);
                        }

                        //Uso esa clave para buscar el nombre del autor. Esto devuelve OTRO futuro.
                        CompletableFuture<String> authorNameFuture = authorDAO.findAuthorNameByKey(authorKey);

                        //Cuando el futuro del nombre del autor se complete, creo el objeto Book.
                        return authorNameFuture.thenApply(authorName -> new Book(key, title, authorName));

                    } catch (Exception e) {
                        e.printStackTrace();
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }
}
