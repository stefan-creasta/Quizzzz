package server.service;

import commons.Game;
import commons.GameState;
import commons.Player;
import commons.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceTest {
    @Test
    public void realWorldTest() {
        QuestionService questionService = mock(QuestionService.class);
        when(questionService.getAll()).thenReturn(List.of(
                new Question("a", "b", "c", "d")
        ));
        GameService service = new GameService(questionService);
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
}
