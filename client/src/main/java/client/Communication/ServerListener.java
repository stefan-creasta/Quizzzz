package client.Communication;

import client.scenes.MainCtrl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Inject;
import commons.GameState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerListener {
    private HttpClient client;

    private Thread listeningThread;

    private MainCtrl mainCtrl;

    private static Gson gson = new Gson();

    @Inject
    public ServerListener(HttpClient client) {
        this.client = client;
    }

    public void initialize(final long playerId, MainCtrl mainCtrl) throws IllegalArgumentException {
        if (mainCtrl == null) throw new IllegalArgumentException();
        this.mainCtrl = mainCtrl;
        listeningThread = new Thread(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/listen?playerId=" + playerId))
                    .GET()
                    .build();
            while (true) {
                try {
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    var gameState = (GameState) gson.fromJson(response.body(), new TypeToken<GameState>(){}.getType());
                    this.handler(gameState);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });

        listeningThread.start();
    }

    private void handler(GameState gameState) {
        if (gameState.isError) {
            System.out.println(gameState.message);
        }
        else {
            mainCtrl.handleGameState(gameState);
        }
    }
}
