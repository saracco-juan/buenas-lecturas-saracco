package DAO;

import API.APIClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

//Chequear, es correcto tener todo lo de autorDAO sin modelo author?
public class AuthorDAO {

    private final APIClient apiClient;
    private final ObjectMapper objectMapper;

    public AuthorDAO() {
        this.apiClient = new APIClient();
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<String> findAuthorNameByKey(String authorKey) {
        //Metodo para buscar el nombre de un author a traves de su key

        //Armo la url ---> (se podria mejorar con encode?)
        String url = "https://openlibrary.org" + authorKey + ".json";
        //Ejecuto la peticion
        return apiClient.getAsync(url)
                .thenApply(jsonBody -> {
                    if (jsonBody == null){
                        System.out.println("ERROR: El JsonBody es NULL (FindAuthorNameByKey)");
                        return "Desconocido";
                    }
                    try {
                        //Parseo el JSON
                        JsonNode authorNode = objectMapper.readTree(jsonBody);
                        //El nombre esta en el campo name del authoNode
                        return authorNode.path("name").asText("Desconocido");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Desconocido";
                    }
                });
    }
}
