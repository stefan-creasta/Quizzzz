package server.service;

import commons.Game;
import commons.GameState;
import commons.Player;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Map<Long, DeferredResult<GameState>> playerConnections;

    private final GameRepository gameRepository = new GameRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final QuestionService questionService;

    @Autowired
    public GameService(QuestionService questionService) {
        this.questionService = questionService;
        this.playerConnections = new HashMap<>();
    }

    public Game getId(long id) {
        return gameRepository.getId(id);
    }

    public long createGame() {
        //TODO: Example questions till question import is fixed
        //List<Question> questions = questionService.getAll();

        List<Question> questions = new ArrayList<>();
        questions = questionService.getAll();
        Game g = new Game(questions);
        gameRepository.save(g);
        return g.id;
    }

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

    public void initiateGame(long gameId) throws IllegalArgumentException {
        Game game = gameRepository.getId(gameId);
        if (game.started) return;
        game.started = true;
        questionPhase(game);
    }

    public void questionPhase(final Game game) {

        GameState state = new GameState(game.id, questionService.getId(game.questions.get(game.currentQuestion).id), null);

        for (Player player : game.players) {
            state.setPlayer(player);
            state.stage = GameState.Stage.QUESTION;
            sendToPlayer(player.id, state);
        }

        //switch to interval phase 30 seconds later so we make sure all player timers have run out.
        //TODO: have a flexible delay in case everyone's timer is shortened.
        new Thread(() -> {
            try {
                Thread.sleep(10_000);
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
        score(game);
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

    /**
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

    public void submitByPlayer(Long playerId, String ans, Long gameId) {
        Game g = gameRepository.getId(gameId);
        Player p;
        int pos = 0;
        while(pos<g.players.size() && g.players.get(pos).id != playerId){
            pos++;
        }
        p = g.players.get(pos);
        if(p.answer == null || p.answer.isEmpty())
            p.answer = ans;
    }

    public void score(Game g) {
        System.out.println("Scores:");
        Question q = g.questions.get(g.currentQuestion);
        for(Player p: g.players){
            if(p.answer.equals(q.answer))
                p.score ++;
            p.answer = null;
            System.out.println(p.id + ": " + p.score);
        }
    }
}
