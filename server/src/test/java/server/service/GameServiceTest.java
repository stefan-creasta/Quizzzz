package server.service;

import commons.*;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.web.context.request.async.DeferredResult;
import server.api.GameController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    QuestionService questionService;
    LongPollingService longPollingService;
    GameService gameService;
    long gameId;
    GameState state;
    Game currentGame;
    Player player;
    @BeforeEach
    void setup() {
        questionService = mock(QuestionService.class);
        longPollingService = new LongPollingService();
        when(questionService.getRandom()).thenReturn(List.of(
                new Question(0, "question", "answer", "wrong1", "wrong2", "1")
        ));
        gameService = new GameService(questionService, longPollingService);
        gameId = gameService.createGame();
        state = gameService.joinGame(gameId, "ben");
        currentGame = gameService.getId(gameId);
        player = currentGame.players.get(0);
    }


    @Test
    public void realWorldTest() {
        QuestionService questionService = mock(QuestionService.class);
        LongPollingService longPollingService = mock(LongPollingService.class);
        when(questionService.getRandom()).thenReturn(List.of(
                new Question(0, "question", "answer", "wrong1", "wrong2", "1")
        ));
        GameService service = new GameService(questionService, longPollingService);
        long gameId = service.createGame();
        GameState state = service.joinGame(gameId, "group53");
        Game currentGame = service.getId(gameId);
        Player player = currentGame.players.get(0);

        assertEquals("group53", player.username);
        assertEquals(GameState.Stage.LOBBY, currentGame.stage);

        state = currentGame.getState();
        state = currentGame.getState(player);

        assertEquals(gameId, state.gameId);
    }


    @Test
    public void testHalfTimePowerUp(){
        assertEquals("halfTimePowerUp___success___",gameService.halfTimePowerUp(player.id, gameId));
    }


    @Test
    public void testDoublePoints(){
        assertEquals("doublePointsPowerUp___success",gameService.doublePointsPowerUp(player.id, gameId));
    }


    @Test
    public void testEliminateWrong(){
        String result = gameService.eliminateWrongAnswerPowerUp(player.id, gameId);
        assertTrue(result.equals("eliminateWrongAnswerPowerUp___success___wrong1")||
                result.equals("eliminateWrongAnswerPowerUp___success___wrong2"));
    }


    @Test
    public void getGameTest(){
        GameService gameService = mock(GameService.class);
        GameController gameController = new GameController(gameService);
        assertNull(gameService.getId(1L));
    }


    @Test
    public void createCurrentGameTest(){
        gameService.createGame();
        Mockito.verify(questionService, times(1)).getRandom();
    }

    @Test
    public void getIdTest(){
        long id = gameService.createSingleplayerGame();
        Game game = gameService.getId(id);
        assertNotNull(game);

        assertThrows(IllegalArgumentException.class, () -> {
            gameService.getId(123456789);
        });
    }

    @Test
    public void createGameTest(){
        assertEquals(gameService.createGame(), gameService.createGame());
    }

    @Test
    public void createSingleplayerGameTest(){
        assertEquals(gameService.createGame(), gameService.createGame());
    }

    @Test
    public void initiateGameTest() {
        long id = gameService.createGame();

        gameService.initiateGame(id);

        Game game = gameService.getId(id);

        assertEquals(GameState.Stage.QUESTION, game.stage);
        assertTrue(game.started);
        assertEquals(0, game.currentQuestion);
    }

    @Test
    public void initiateSingleplayerGameTest() {
        long id = gameService.createGame();

        gameService.initiateGame(id);

        Game game = gameService.getId(id);

        assertEquals(GameState.Stage.QUESTION, game.stage);
        assertTrue(game.started);
        assertEquals(0, game.currentQuestion);
    }

    @Test
    public void JoinGameTest() {
        long id = gameService.createGame();

        GameState state = gameService.joinGame(id, "Napoleon Bonaparte");

        assertEquals("Napoleon Bonaparte", state.username);
        assertEquals(0, state.thisScored);
        assertNull(state.playerAnswer);
    }

    @Test
    public void JoinSingleplayerGameTest() {
        long id = gameService.createSingleplayerGame();

        GameState state = gameService.joinSingleplayerGame(id, "Napoleon Bonaparte");

        assertEquals("Napoleon Bonaparte", state.username);
        assertEquals(0, state.thisScored);
        assertNull(state.playerAnswer);
    }

    @Test
    public void GetPlayersTest() {
        long id = gameService.createSingleplayerGame();

        gameService.joinSingleplayerGame(id, "Wendy Carlos");

        List<String> players = gameService.getPlayers(id);
        assertEquals(1, players.size());
        assertTrue(players.contains("Wendy Carlos"));
    }

    @Test
    public void getLeaderboard() {
        long id = gameService.createSingleplayerGame();

        gameService.joinSingleplayerGame(id, "Andy Zaidman");

        List<LeaderboardEntry> leaderboard = gameService.getLeaderboard(id);

        assertEquals(1, leaderboard.size());
        assertEquals(1, leaderboard.stream().filter(x -> x.username.equals("Andy Zaidman")).count());
    }

    @Test
    public void checkUsernameTest() {
        long id = gameService.createGame();

        assertTrue(gameService.checkUsername(id, "Christiaan Huygens"));

        gameService.joinGame(id, "Christiaan Huygens");

        assertFalse(gameService.checkUsername(id, "Christiaan Huygens"));
    }

    @Test
    public void existsByIdTest() {
        long id = gameService.createSingleplayerGame();

        assertTrue(gameService.existsById(id));
        assertFalse(gameService.existsById(123456789));
    }

    @Test
    public void halfTimePowerUpTest() {
        long gid = gameService.createGame();
        Game game = gameService.getId(gid);

        GameState s1 = gameService.joinGame(gid, "Fire Boy");
        GameState s2 = gameService.joinGame(gid, "Water Girl");
        Player p1 = game.players.stream()
                .filter(x -> x.username.equals("Fire Boy"))
                        .findFirst().get();
        Player p2 = game.players.stream()
                .filter(x -> x.username.equals("Water Girl"))
                .findFirst().get();

        DeferredResult<List<GameState>> dr1 = new DeferredResult<>();
        DeferredResult<List<GameState>> dr2 = new DeferredResult<>();

        gameService.halfTimePowerUp(p1.id, gid);

        longPollingService.registerPlayerConnection(p1.id, dr1);
        longPollingService.registerPlayerConnection(p2.id, dr2);

        gameService.initiateGame(gid);

        assertTrue(dr1.hasResult());
        assertTrue(dr2.hasResult());

        var l1 = (List<GameState>) dr1.getResult();
        var l2 = (List<GameState>) dr2.getResult();
        s1 = l1.get(l1.size() - 1);
        s2 = l2.get(l2.size() - 1);

        assertNotEquals("halfTimePowerUp", s1.instruction);
        assertEquals("halfTimePowerUp", s2.instruction);
    }
}
