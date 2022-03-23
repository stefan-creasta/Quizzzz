package client.Communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import client.scenes.*;
import com.google.inject.Inject;
import commons.GameState;
import javafx.application.Platform;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerListener {
    private HttpClient client;

    private Thread listeningThread;

    public MainCtrl mainCtrl;

    //private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Inject
    public ServerListener(HttpClient client) {
        this.client = client;
    }

    /**Initialize the listener with the playerId received from joinGame and the mainCtrl that will handleGameState
     * Needs to be called as soon as the player wants to see up-to-date lobby and game session information.
     * @param playerId the playerId that is retrieved in a GameState object
     * @param mainCtrl the mainCtrl that will handleGameState
     * @throws IllegalArgumentException thrown when mainCtrl is null
     */
    public void initialize(final long playerId, MainCtrl mainCtrl) throws IllegalArgumentException {
        if (mainCtrl == null) throw new IllegalArgumentException();
        this.mainCtrl = mainCtrl;
        final ObjectMapper mapper = new ObjectMapper();
        final TypeReference<GameState> typeRef = new TypeReference<>() {};
        // New threads need to be invoked via the JavaFX Platform API, otherwise it won't run
        listeningThread = new Thread(() -> {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/listen?playerId=" + playerId))
                    .GET()
                    .build();
            while (true) {
                System.out.println("loop is running");
                try {
                    var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    //GameState gameState = gson.fromJson(response.body(), new TypeToken<GameState>(){}.getType());
                    System.out.println(response.body());
                    GameState gameState = mapper.readValue(response.body(), typeRef);
                    System.out.println(response.body());
                    System.out.println(gameState);
                    Platform.runLater(() -> this.handler(gameState));
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

    /**Immediate step to prevent any erroneous GameStates from being passed to the rest of the client application.
     * @param gameState the received GameState
     */
    private void handler(GameState gameState) {
        if (gameState.isError) {
            System.out.println(gameState.message);
        }
        else {
            mainCtrl.handleGameState(gameState);
        }
    }
}
