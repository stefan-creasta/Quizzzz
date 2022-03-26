package client.Communication;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import commons.EmoteClass;
import commons.GameState;
import commons.PlayerAnswer;
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


    /**
     * Gets called when an answer button is pressed. Sends the contents of the button as a string, the gameID and the playerID
     * using a POST HTTP request.
     * @param answer String containing the contents of the answer button that got pressed
     * @param gameState the gameState(game instance) that is player
     * @throws IOException can get thrown
     * @throws InterruptedException can get thrown
     */
    public static void sendAnswer(String answer, GameState gameState) throws IOException, InterruptedException {

        PlayerAnswer ans = new PlayerAnswer(answer, gameState.gameId, gameState.playerId);

        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(ans);

        System.out.println("\nAnswer sent to server:\n" + answer);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/answer"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Gets the current question that is displayed during the QUESTION state of a game using a GET HTTP request.
     * @param gameId the gameID of the gameState
     * @return the Question entity of the question
     * @throws IOException can get thrown
     * @throws InterruptedException can get thrown
     */
    public static Question getQuestion(long gameId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/"+gameId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<Question>(){}.getType());
    }

    /**
     * Sends the emote and gameId to the server, so it can update the list of emotes pressed.
     * @param emote
     * @throws IOException
     * @throws InterruptedException
     */
    public static void sendEmote(EmoteClass emote) throws IOException, InterruptedException {
        var objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(emote);

        System.out.println("\nEmote sent to server:\n" + emote.emote.name());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/answer/emote"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
