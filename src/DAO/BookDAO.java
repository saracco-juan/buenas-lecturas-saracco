package DAO;

import API.APIClient;
import Model.Book;
import Model.Response;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
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

    //Al crear la clase, instancio el api client y el object mapper
    public BookDAO() {
        this.apiClient = new APIClient(); // Crea la instancia
        this.objectMapper = new ObjectMapper();
    }

    //Metodo para buscar libro por titulo
    public CompletableFuture<List<Book>> searchByTitle(String title){

        // 1. Construir la URL de forma segura (codificando el título)
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String url = "https://openlibrary.org/search.json?q=" + encodedTitle;

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
                           String bookTitle = bookNode.path("title").asText();
                           String isbn = bookNode.path("isbn").get(0).asText(); // ISBN es un array, tomamos el primero.

                           // El autor también es un array.
                           String authorName = "Desconocido";
                           if (bookNode.has("author_name")) {
                               authorName = bookNode.path("author_name").get(0).asText();
                           }

                           // Creamos la instancia del libro. (Asegúrate de tener un constructor adecuado en tu clase Book).
                           Book book = new Book(isbn, bookTitle, authorName);
                           books.add(book);
                       }
                       return books;

                   } catch (Exception e) {
                       e.printStackTrace();
                       return Collections.<Book>emptyList();
                   }
               });
    }
}
