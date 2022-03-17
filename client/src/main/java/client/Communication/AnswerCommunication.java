package client.Communication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import commons.GameState;
import commons.Question;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;


public class AnswerCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Gson gson = new Gson();

    public static void sendAnswer(int answer, GameState gameState) throws IOException, InterruptedException {

        Map<String, String> values = new HashMap<String, String>() {{
            put("gameId", String.valueOf(gameState.playerId));
            put ("playerId", String.valueOf(gameState.gameId));
        }};

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(values);

        System.out.println("\nAnswer sent to server:\n" + answer);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/answer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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
