package API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class APIClient {

    //Con este atributo voy a gestionar las conexiones a la API, sirve para reutilzar la peticion
    private static final HttpClient client = HttpClient.newBuilder().build();


    //Este metodo realiza una peticion GET de forma asincrona, es decir no bloquea el programa mientras espera la repsuesta de la red
    //Recibe por parametro la url completa a la que hara la peticion GET

    //El objeto "CompletableFuturo" es una promesa de que un resultado estara disponible en el futuro
    public CompletableFuture<String> getAsync(String url) {

        //Este objeto representa lo que voy a enviar a la API
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json")
                .build();

        //Envio la peticion de forma asincrona
        return  client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                //Proceso la respuesta cuando llegue
                .thenApply(HttpResponse::body)

                //Manejo cualquie tipo de error de la red
                .exceptionally(e -> {
                    System.err.println("ERROR: Error en la llamada a la API en API CLIENT" + e.getMessage());
                    return null;
                });

    }
}
