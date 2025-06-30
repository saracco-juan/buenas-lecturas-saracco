package DAO;

import API.APIClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

public class AuthorDAO {

    private final APIClient apiClient;
    private final ObjectMapper objectMapper;

    public AuthorDAO() {
        this.apiClient = new APIClient();
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<String> findAuthorNameByKey(String authorKey) {
        String url = "https://openlibrary.org" + authorKey + ".json";
        return apiClient.getAsync(url)
                .thenApply(jsonBody -> {
                    if (jsonBody == null) return "Desconocido";
                    try {
                        JsonNode authorNode = objectMapper.readTree(jsonBody);
                        // El nombre está en el campo "name"
                        return authorNode.path("name").asText("Desconocido");
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "Desconocido";
                    }
                });
    }
}
