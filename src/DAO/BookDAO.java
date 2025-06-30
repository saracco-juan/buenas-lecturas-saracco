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

    private final AuthorDAO authorDAO;

    //Al crear la clase, instancio el api client y el object mapper
    public BookDAO() {
        this.apiClient = new APIClient(); // Crea la instancia
        this.objectMapper = new ObjectMapper();
        this.authorDAO = new AuthorDAO();
    }

    //Metodo para buscar libro por titulo
    public CompletableFuture<List<Book>> searchByTitle(String title){

        // 1. Construir la URL de forma segura (codificando el título)
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = "https://openlibrary.org/search.json?q=" + encodedTitle;

        System.out.println("4. BookRepository va a llamar a la API con la URL: " + url);


        //Delego la llamado al APIclient
       return apiClient.getAsync(url)
               .thenApply(jsonBody -> {

                   //Validacion por si falla la peticion
                   if(jsonBody == null){
                       return Collections.emptyList();
                   }

                   //Parseo el String JSON a un objeto Java
                   try {
                       List<Book> books = new ArrayList<>();
                       JsonNode rootNode = objectMapper.readTree(jsonBody);
                       JsonNode docsNode = rootNode.path("docs"); // "docs" es el array que contiene los resultados

                       for (JsonNode bookNode : docsNode) {
                           // Por cada elemento en el array "docs", creamos un objeto Book.

                           // Extraemos los campos que nos interesan. Usamos .path() que no lanza error si el campo no existe.
                           String workId = bookNode.path("key").asText(); // <-- Obtiene la clave
                           String bookTitle = bookNode.path("title").asText("Titulo no disponible");
                           //String isbn = "N/A";


                           //if (bookNode.has("isbn") && bookNode.get("isbn").isArray() && bookNode.get("isbn").size() > 0) {
                               // 2. Solo si se cumplen las condiciones, obtén el primer elemento.
                           //    isbn = bookNode.get("isbn").get(0).asText();
                           //}

                           // El autor también es un array.
                           String authorName = "Desconocido";

                           if (bookNode.has("author_name") && bookNode.get("author_name").isArray() && bookNode.get("author_name").size() > 0) {
                               authorName = bookNode.get("author_name").get(0).asText();
                           }

                           // Creamos la instancia del libro. (Asegúrate de tener un constructor adecuado en tu clase Book).
                           Book book = new Book(workId, bookTitle, authorName);
                           books.add(book);
                       }
                       return books;

                   } catch (Exception e) {
                       e.printStackTrace();
                       return Collections.<Book>emptyList();
                   }
               });
    }

    //Metodo para buscar libro por key
    public CompletableFuture<Book> findByKey(String key) {
        // La URL se construye a partir de la clave
        String url = "https://openlibrary.org" + key + ".json";

//        return apiClient.getAsync(url)
//                .thenApply(jsonBody -> {
//                    if (jsonBody == null) return null;
//                    try {
//                        JsonNode bookNode = objectMapper.readTree(jsonBody);
//                        // Parseamos los detalles de este libro único
//                        String title = bookNode.path("title").asText("Sin Título");
//                        // ... podrías obtener más detalles aquí ...
//
//                        // Necesitamos el nombre del autor, que puede requerir otra llamada o parseo complejo
//                        String authorName = "Desconocido"; // Simplificación
//
//                        return new Book(key, title, authorName);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                });
        return apiClient.getAsync(url)
                .thenCompose(jsonBody -> { // ¡Usamos thenCompose para encadenar futuros!
                    if (jsonBody == null) {
                        // Si la primera llamada falla, devolvemos un futuro completado con null.
                        return CompletableFuture.completedFuture(null);
                    }
                    try {
                        JsonNode bookNode = objectMapper.readTree(jsonBody);
                        String title = bookNode.path("title").asText("Sin Título");

                        // 1. Extraer la clave del autor del JSON del libro.
                        JsonNode authorNode = bookNode.path("authors").get(0).path("author").path("key");
                        String authorKey = authorNode.asText();

                        if (authorKey.isEmpty()) {
                            // Si no hay autor, creamos el libro con "Desconocido".
                            Book book = new Book(key, title, "Desconocido");
                            return CompletableFuture.completedFuture(book);
                        }

                        // 2. Usamos esa clave para buscar el nombre del autor. Esto devuelve OTRO futuro.
                        CompletableFuture<String> authorNameFuture = authorDAO.findAuthorNameByKey(authorKey);

                        // 3. Cuando el futuro del nombre del autor se complete, creamos el objeto Book.
                        return authorNameFuture.thenApply(authorName -> new Book(key, title, authorName));

                    } catch (Exception e) {
                        e.printStackTrace();
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }
}
