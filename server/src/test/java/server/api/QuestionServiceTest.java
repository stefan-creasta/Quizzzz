package server.api;

import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.QuestionRepository;
import server.service.QuestionService;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuestionServiceTest {

    QuestionRepository qRepo = mock(QuestionRepository.class);
    QuestionService qService = new QuestionService(qRepo);
    Question question = new Question(0, "question", "answer", "wrong1", "wrong2", "1");

    @BeforeEach
    public void setup(){
        when(qRepo.save(any(Question.class))).thenReturn(question);
    }



    @Test
    void addNewQuestionTest() {
        Question returnedQuestion = qService.addNewQuestion(question);
        assertEquals(returnedQuestion, question);
        verify(qRepo, times(1)).save(any(Question.class));
    }


    @Test
    void existsByIdTest1() {
        when(qRepo.existsById(any(long.class))).thenReturn(true);
        assertTrue(qService.existsById(1));
    }

    @Test
    void existsByIdTest2() {
        when(qRepo.existsById(any(long.class))).thenReturn(false);
        assertFalse(qService.existsById(1));
    }

    @Test
    void getIdTest() {
        when(qRepo.getId(any(long.class))).thenReturn(question);
        assertEquals(question, qService.getId(1));
    }

    @Test
    void getAllTest() {
        List<Question> questions = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            questions.add(new Question(i, "what", "1", "2", "3", Integer.toString(i % 4)));
        }
        when(qRepo.getAll()).thenReturn(questions);
        assertEquals(questions, qService.getAll());
    }

    @Test
    void getRandomTest() {
        List<Question> questions = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            questions.add(new Question(i, "what", "1", "2", "3", Integer.toString(i % 4)));
        }
        when(qRepo.getAll()).thenReturn(questions);
        for (Question q : qService.getRandom()) {
            assertTrue(questions.contains(q), "This question is not in the database: " + q);
        }
    }

    @Test
    void searchTest() {
        List<Question> questions = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            questions.add(new Question(i, "what", "1", "2", "3", "3"));
        }
        when(qRepo.getAll()).thenReturn(questions);
        when(qRepo.searchWithWattsAnswer(any(String.class))).thenReturn(questions);
        assertEquals(questions, qService.search("what"));
    }

    @Test
    void patchQuestionNonexistentIdTest() {
        /*
        Question unchanged = new Question(69L, "correct", "0", "1", "2", "1");
        Question changed = new Question(70L, "wrong", "0", "1", "2", "1");
        when(qRepo.getId(eq(69L))).thenReturn(unchanged);
        when(qRepo.getId(eq(70L))).thenReturn(changed);
        when(qRepo.existsById(eq(69L))).thenReturn(true);
        when(qRepo.existsById(eq(70L))).thenReturn(true);
        when(qRepo.existsById(eq(71L))).thenReturn(false);

        Question wrongPatch = new Question(71L, null, null, null, null, null);
        assertNull(qService.patchQuestion(wrongPatch));
        Question blankPatch = new Question(69L, null, null, null, null, null);
        assertEquals(unchanged, qService.patchQuestion(blankPatch));
        Question changePatch = new Question(70L, "correct", null, null, null, "3");
        assertEquals(
                new Question(70L, "correct", "0", "1", "2", "3"),
                qService.patchQuestion(changePatch)
        );
        Question answerPatch = new Question(70L, null, "100000000000000000000000000", null, null, null);
        Question answerPatched = qService.patchQuestion(answerPatch);
        assertEquals("correct", answerPatched.question);
        assertEquals("100000000000000000000000000", answerPatched.answer);
        long ratio =
        assertTrue(Math.abs())

         */


        Question wrongPatch = new Question(71L, null, null, null, null, null);
        when(qRepo.existsById(eq(71L))).thenReturn(false);
        assertNull(qService.patchQuestion(wrongPatch));
    }

    @Test
    void patchQuestionBlankPatchTest() {
        Question unchanged = new Question(69L, "correct", "0", "1", "2", "1");
        when(qRepo.getId(eq(69L))).thenReturn(unchanged);
        when(qRepo.existsById(eq(69L))).thenReturn(true);
        Question blankPatch = new Question(69L, null, null, null, null, null);
        assertEquals(unchanged, qService.patchQuestion(blankPatch));
    }

    @Test
    void patchQuestionPatchTest() {
        Question changed = new Question(70L, "wrong", "0", "1", "2", "1");
        when(qRepo.getId(eq(70L))).thenReturn(changed);
        when(qRepo.existsById(eq(70L))).thenReturn(true);
        Question changePatch = new Question(70L, "correct", null, null, null, "3");
        assertEquals(
                new Question(70L, "correct", "0", "1", "2", "3"),
                qService.patchQuestion(changePatch)
        );
        Question answerPatch = new Question(70L, "correct", "10000000000000000000000000", null, null, null);
        Question answerPatched = qService.patchQuestion(answerPatch);
        assertEquals("correct", answerPatched.question);
        assertEquals("10000000000000000000000000", answerPatched.answer);
        double answer = 1e25;
        double ratio1 = Double.parseDouble(answerPatched.wrongAnswer1) / answer;
        double ratio2 = Double.parseDouble(answerPatched.wrongAnswer2) / answer;
        assertTrue(0.2 <= ratio1 && ratio1 < 5., answerPatched.wrongAnswer1 + " is not in the required range");
        assertTrue(0.2 <= ratio2 && ratio2 < 5., answerPatched.wrongAnswer2 + " is not in the required range");
    }

    @Test
    void deleteQuestionTest() {
        qService.deleteQuestion(0L);
    }

    @Test
    void deleteAllTest() {
        qService.deleteAll();
    }
}
