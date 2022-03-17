package server.service;

import commons.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        public Map<Long, Player> getplayermaps(){
            return this.players;
        }
    }
    private LobbyRepository lobbyrepository = new LobbyRepository();
    public LobbyService(){
    }
    public Player addPlayer(Player player){
        lobbyrepository.save(player);
        return player;
    }
    public List<Player> returnPlayer(){
        Map <Long, Player> playerMap = lobbyrepository.getplayermaps();
        List<Player> playerlist = new ArrayList<>();
        for(Long playerId : playerMap.keySet()) {
            playerlist.add(playerMap.get(playerId));
        }
        return playerlist;
    }

}
