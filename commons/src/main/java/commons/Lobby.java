package commons;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    private List<Player> playerList;

    public Lobby() {
        playerList = new ArrayList<>();
    }
    public List<Player> getPlayerList() {
        return playerList;
    }
    public void addPlayer(Player player) {
        playerList.add(player);
    }
}
