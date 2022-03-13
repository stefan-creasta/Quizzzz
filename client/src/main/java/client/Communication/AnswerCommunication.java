package client.Communication;


import com.google.common.reflect.TypeToken;
import commons.Question;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;


public class AnswerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Gson gson = new Gson();

    public static void sendAnswer(String message, long id) throws IOException, InterruptedException {
        message = id + "_" + message;
        System.out.println("\nAnswer sent to server:\n" + message);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/answer"))
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static Question getQuestion(long gameId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/"+gameId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<Question>(){}.getType());
    }

}
