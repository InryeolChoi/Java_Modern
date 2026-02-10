package part15.section3;

import java.net.*;
import java.net.http.*;
import java.util.concurrent.CompletableFuture;

public class AsyncApiClient {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<Boolean> callTodoAsync(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/" + id))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> body.contains("\"completed\": true"));
    }
}