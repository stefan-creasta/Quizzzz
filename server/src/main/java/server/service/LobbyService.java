package server.service;

import commons.Player;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LobbyService {
    class LobbyRepository{
        private Map<Long, Player> players;

        LobbyRepository() {
            players = new HashMap<>();
        }

        Player getId(long id) throws IllegalArgumentException {
            if (existsById(id)) throw new IllegalArgumentException();
            return players.get(id);
        }

        void save(Player player) {
            players.put(player.id, player);
        }

        boolean existsById(long id) {
            return players.containsKey(id);
        }
    }
    private LobbyRepository lobbyrepository = new LobbyRepository();
    public LobbyService(){
    }
    public Player addPlayer(Player player){
        lobbyrepository.save(player);
        return player;
    }

}
