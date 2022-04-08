package server.service;

import commons.Game;
import commons.GameState;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.Test;
import server.api.GameController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceTest {

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
    public void testHalfTimePowerUpFail(){
        GameService gameService = mock(GameService.class);
        assertEquals(null,gameService.halfTimePowerUp(-1,-1));

    }
    @Test
    public void testEliminatePowerUpFail(){
        GameService gameService = mock(GameService.class);
        assertEquals(null,gameService.halfTimePowerUp(-1,-1));


    }
    @Test
    public void testDoublePointsFail(){
        GameService gameService = mock(GameService.class);
        assertEquals(null,gameService.halfTimePowerUp(-1,-1));


    }
    @Test
    public void getGameTest(){
        GameService gameService = mock(GameService.class);
        GameController gameController = new GameController(gameService);
        assertNull(gameService.getId(1L));

    }




}
