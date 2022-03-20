package server.service;

import commons.Game;
import commons.GameState;
import commons.Player;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;

@Service
public class GameService {
    class GameRepository {
        private Map<Long, Game> games;

        GameRepository() {
            games = new HashMap<Long, Game>();
        }

        Game getId(long id) throws IllegalArgumentException {
            if (!existsById(id)) throw new IllegalArgumentException();
            return games.get(id);
        }

        void save(Game game) {
            Objects.requireNonNull(game, "game cannot be null");
            games.put(game.id, game);
        }

        boolean existsById(long id) {
            return games.containsKey(id);
        }
    }

    class PlayerRepository {
        private Map<Long, Player> players;

        PlayerRepository() {
            players = new HashMap<>();
        }

        Player getId(long id) throws IllegalArgumentException {
            if (!existsById(id)) throw new IllegalArgumentException();
            return players.get(id);
        }

        void save(Player player) {
            Objects.requireNonNull(player, "player cannot be null");
            players.put(player.id, player);
        }

        boolean existsById(long id) {
            return players.containsKey(id);
        }
    }

    private Map<Long, DeferredResult<GameState>> playerConnections;

    private final GameRepository gameRepository = new GameRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();

    @Autowired
    public GameService() {
        this.playerConnections = new HashMap<>();
    }

    public Game getId(long id) {
        return gameRepository.getId(id);
    }

    /**Produce a gameId which the player client can join a game or its lobby with
     *
     * This method can be used even if there is only one lobby that can be joined at a given moment. Then it would
     * return the gameId of the game that the player is wanted to join.
     * @return the gameId
     */
    public long createGame() {
        //TODO: Example questions till question import is fixed
        //List<Question> questions = questionService.getAll();

        List<Question> questions = new ArrayList<>();
        questions.add(new Question("This is an example question?","Answer1","Wrong1","Wrong2"));

        Game g = new Game(questions);
        gameRepository.save(g);
        return g.id;
    }

    /**Register *one player* into a game with the given username. This will fail if the lobby of the game already
     * has a player with the given username.
     *
     * @param gameId the id of the game to join
     * @param username the username that the player wants to join with
     * @return the GameState to initially render the lobby or game screen
     * @throws IllegalArgumentException if the gameId is invalid.
     */
    public GameState joinGame(long gameId, String username) throws IllegalArgumentException {
        Player player = new Player(username, 0);
        Game game = gameRepository.getId(gameId);
        GameState state = new GameState(gameId, game.getCurrentQuestion(), player);
        if (!game.addPlayer(player)) {
            state.isError = true;
            state.message = "usernameAlreadyInGame";
            return state;
        }
        playerRepository.save(player);
        return state;
    }

    /**Initiate a game. Do not allow other players to join and show the first question.
     *
     * @param gameId the id of the game to initiate
     * @throws IllegalArgumentException if the gameId is invalid.
     */
    public void initiateGame(long gameId) throws IllegalArgumentException {
        Game game = gameRepository.getId(gameId);
        if (game.started) return;
        game.started = true;
        questionPhase(game);
    }

    //methods that call each other back and forth to have a single game running.
    public void questionPhase(final Game game) {

        GameState state = new GameState(game.id, game.getCurrentQuestion(), null);

        for (Player player : game.players) {
            state.setPlayer(player);
            state.stage = GameState.Stage.QUESTION;
            sendToPlayer(player.id, state);
        }

        //switch to interval phase 30 seconds later so we make sure all player timers have run out.
        //TODO: have a flexible delay in case everyone's timer is shortened.
        new Thread(() -> {
            try {
                Thread.sleep(5_000);
                intervalPhase(game);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void intervalPhase(final Game game) {
         GameState state = new GameState(game.id, game.getCurrentQuestion(), null);
        state.stage = GameState.Stage.INTERVAL;
        for (Player player : game.players) {
            state.setPlayer(player);
            sendToPlayer(player.id, state);
        }
        //switch to question phase 5 seconds later so the player has time to see the correctness of their answer
        if (game.progressGame()) {
            new Thread(() -> {
                try {
                    Thread.sleep(5_000);
                    questionPhase(game);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public boolean existsById(long id) {
        return gameRepository.existsById(id);
    }

    public void registerPlayerConnection(Long playerId, DeferredResult<GameState> result) {
        playerConnections.put(playerId, result);
    }

    /**Send the game state to a player.
     *
     * Note that the connection is only available after the player makes a request to
     * /api/listen, therefore you can't just send new data to a player without some
     * delay in between!
     *
     * @param playerId
     * @param state
     */
    public void sendToPlayer(Long playerId, GameState state) {
        DeferredResult<GameState> connection = playerConnections.get(playerId);
        if (connection != null) connection.setResult(state);
        playerConnections.put(playerId, null);
    }
}
