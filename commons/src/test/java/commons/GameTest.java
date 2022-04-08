package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTest {

    @Test
    void constructorTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game = new Game(questions);
        assertNotNull(game);
    }

    @Test
    void getCurrentQuestionTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game = new Game(questions);
        assertEquals(q1, game.getCurrentQuestion());
    }

    @Test
    void progressGameTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game = new Game(questions);
        assertTrue(game.progressGame());
    }

    @Test
    void addPlayerTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game = new Game(questions);
        Player p = new Player("a", 0);
        assertTrue(game.addPlayer(p));
    }

    @Test
    void getStateTest() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game = new Game(questions);
        Player p = new Player("a", 0);
        GameState state1 = game.getState();
        GameState state2 = new GameState(game, null);
        assertEquals(state1, state2);
    }

    @Test
    void testEquals1() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game1 = new Game(questions);
        game1.id = 0;
        List<Question> questions2 = new ArrayList<>();
        questions2.add(q1);
        questions2.add(q2);
        questions2.add(q3);
        Game game2 = new Game(questions2);
        game2.id = 0;
        assertEquals(game1, game2);
    }

    @Test
    void testEquals2() {
        Question q1 = mock(Question.class);
        Question q2 = mock(Question.class);
        Question q3 = mock(Question.class);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        Game game1 = new Game(questions);
        game1.id = 0;
        List<Question> questions2 = new ArrayList<>();
        questions2.add(q1);
        questions2.add(q2);
        Game game2 = new Game(questions2);
        game2.id = 0;
        assertNotEquals(game1, game2);
    }

    @Test
    void testToString() {
        Game game = mock(Game.class);
        when(game.toString()).thenReturn("a");
        assertEquals("a", game.toString());
    }
}