package server.service;

import commons.GameState;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class LongPollingService {
    private Map<Long, DeferredResult<List<GameState>>> playerConnections;
    private Map<Long, List<GameState>> playerGameStateQueues;

    public LongPollingService() {
        playerConnections = new HashMap<>();
        playerGameStateQueues = new HashMap<>();
    }

    /**Adds keys to the maps if necessary, and puts the necessary initial values.
     * @param playerId
     */
    private void registerPlayer(Long playerId) {
        if (!playerConnections.containsKey(playerId)) playerConnections.put(playerId, null);
        if (!playerGameStateQueues.containsKey(playerId)) playerGameStateQueues.put(playerId, new LinkedList<>());
    }

    /**Called when the player makes a long polling GET request
     * @see server.api.ListenController
     * @param playerId the playerId that comes with the request
     */
    public synchronized void registerPlayerConnection(Long playerId, DeferredResult<List<GameState>> result) {
        registerPlayer(playerId);
        playerConnections.put(playerId, result);
        sendToPlayer(playerId);
    }

    /**Sends the player the GameState if possible. Otherwise, it queues the GameState until a connection is available.
     * @param playerId
     * @param state
     */
    public synchronized void sendToPlayer(Long playerId, GameState state) {
        registerPlayer(playerId);
        playerGameStateQueues.get(playerId).add(state);
        sendToPlayer(playerId);
    }

    /**Sends the player any queued GameStates if possible. Otherwise, it queues the GameState until a connection is available.
     *
     * @param playerId
     */
    private void sendToPlayer(Long playerId) {
        var connection = playerConnections.get(playerId);
        var queue = playerGameStateQueues.get(playerId);
        //if a connection is currently available.
        //it is available when the method is called from registerPlayerConnection.
        //it might not be available when the method is called from sendToPlayer.
        if (connection != null && !queue.isEmpty()) {
            connection.setResult(queue);
            playerGameStateQueues.put(playerId, new LinkedList<>());
            playerConnections.put(playerId, null);
        }
    }

}
