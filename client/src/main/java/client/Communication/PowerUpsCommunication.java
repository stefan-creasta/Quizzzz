package client.Communication;



import commons.GameState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class PowerUpsCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Called when a player uses any power up. Sends the server a string saying the power up name,
     * the playerID and the gameID. Returns a specific string regarding the result of the use of the power up.
     * String contains different information according to which power up was used.
     * @param message String code depicting the power up used
     * @param state the gameState
     * @return String with information about the use of the power up.
     * @throws IOException can get thrown
     * @throws InterruptedException can get thrown
     */
    public static String sendPowerUps(String message, GameState state) throws IOException, InterruptedException {
        System.out.println("\nPower ups sent to server: \n" + message);

        message = message + "___" + state.playerId + "___" + state.gameId;


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/powerup/" + message))
                .GET()
                .build();
        try{
            HttpResponse<String> answerMessageResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            String result = answerMessageResponse.body();

            return result;

        }
        catch(Exception exc){
            exc.printStackTrace();
        }
        return null;
    }



}

