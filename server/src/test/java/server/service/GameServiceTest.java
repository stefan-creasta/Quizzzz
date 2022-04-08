package server.service;

import commons.Game;
import commons.GameState;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.GameController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        longPollingService = mock(LongPollingService.class);
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
    public void getGameTest(){
        GameService gameService = mock(GameService.class);
        GameController gameController = new GameController(gameService);
        assertNull(gameService.getId(1L));

    }




}
