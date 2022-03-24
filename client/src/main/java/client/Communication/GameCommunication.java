package client.Communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.inject.Inject;
import commons.GameState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GameCommunication {
    private HttpClient client;

    private static Gson gson = new Gson();

    @Inject
    public GameCommunication(HttpClient client) {
        this.client = client;
    }

    public long createGame() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/create"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            return Long.parseLong(client.send(request, HttpResponse.BodyHandlers.ofString()).body());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public GameState joinGame(long gameId, String username) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/join/" + gameId + "?username=" + username))
                .GET()
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            final ObjectMapper mapper = new ObjectMapper();
            final TypeReference<GameState> typeRef = new TypeReference<>() {};
            GameState gameState = mapper.readValue(response.body(), typeRef);
            System.out.println("Leaderboard: " + gameState.leaderboard.size());

            return gameState;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initiateGame(long gameId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/init/" + gameId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<String> getPlayers(long gameId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/game/players/" + gameId))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        final ObjectMapper mapper = new ObjectMapper();
        final TypeReference<List<String>> typeRef = new TypeReference<>() {};
        return mapper.readValue(response.body(), typeRef);
    }
}
