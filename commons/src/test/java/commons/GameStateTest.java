package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameStateTest {

    @Test
    void constructorTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        List<Question> q = new ArrayList<>();
        q.add(q1);
        q.add(q2);
        Game game = new Game(q);
        Player player = mock(Player.class);
        GameState state = new GameState(game, player);
        assertNotNull(state);
    }

    @Test
    void setPlayerTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        List<Question> q = new ArrayList<>();
        q.add(q1);
        q.add(q2);
        Game game = new Game(q);
        Player p1 = new Player("a", 0);
        Player p2 = new Player("b", 1);
        GameState state = new GameState(game, p1);
        state.setPlayer(p2);
        assertEquals(1, state.playerId);
    }

    @Test
    void setPlayerAnswerTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        List<Question> q = new ArrayList<>();
        q.add(q1);
        q.add(q2);
        Game game = new Game(q);
        Player p1 = new Player("a", 0);
        Player p2 = new Player("b", 1);
        GameState state = new GameState(game, p1);
        state.setPlayerAnswer("abc");
        assertEquals("abc", state.playerAnswer);
    }

    @Test
    void toStringTest() {
        GameState state = mock(GameState.class);
        when(state.toString()).thenReturn("abc");
        assertEquals("abc", state.toString());
    }
}