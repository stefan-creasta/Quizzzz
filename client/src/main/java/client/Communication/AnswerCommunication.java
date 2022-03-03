package client.Communication;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class AnswerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    public static void sendAnswer(String message) throws IOException, InterruptedException {
        System.out.println("\nAnswer sent to server:\n" + message);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/answer"))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }



}
