package client.Communication;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import org.glassfish.jersey.client.ClientConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class LobbyCommunication {
    private static final String SERVER = "http://localhost:8080/";
    GameCommunication gameCommunication;
    public LobbyCommunication() {
        gameCommunication = new GameCommunication(HttpClient.newBuilder().build());
    }
    public long getGameId() {
        return gameCommunication.createGame();
    }
    public void joinGame(long gameId, String username) {
        gameCommunication.joinGame(gameId, username);
    }
    public void removePlayersFromLobby() throws IOException, InterruptedException {
        HttpClient.newBuilder().build().send(HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/lobby"))
                .DELETE()
                .build(), HttpResponse.BodyHandlers.ofString());

    }
}
