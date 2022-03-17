package client.Communication;

import commons.Player;
import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class PlayerCommunication {
    private static final String SERVER = "http://localhost:8080/";
    public List<Player> getPlayers() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Player>>() {});
    }

    public Player addPlayer(Player player) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }
}
