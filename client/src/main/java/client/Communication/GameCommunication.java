package client.Communication;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Inject;
import commons.GameState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            return gson.fromJson(response, new TypeToken<GameState>(){}.getType());
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
}
