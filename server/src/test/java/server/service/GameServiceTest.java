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
}
