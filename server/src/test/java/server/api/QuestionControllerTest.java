package server.api;

import commons.Question;
import org.junit.jupiter.api.Test;
import server.service.QuestionService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static server.api.QuestionController.isNullOrEmpty;
import static server.api.QuestionController.isQuestionValid;

public class QuestionControllerTest {

    QuestionService service = mock(QuestionService.class);
    QuestionController controller = new QuestionController(service);
    Question question = new Question("question", "answer", "wrong1", "wrong2");

    @Test
    void getByIdTest1() {
        when(service.existsById(any(long.class))).thenReturn(false);
        when(service.getId(any(long.class))).thenReturn(question);
        assertEquals(null, controller.getById(1));
    }

    @Test
    void getByIdTest2() {
        when(service.existsById(any(long.class))).thenReturn(true);
        when(service.getId(any(long.class))).thenReturn(question);
        assertEquals(question, controller.getById(1));
    }

    @Test
    void isNullOrEmptyTest(){
        assertFalse(isNullOrEmpty("example"));
        assertTrue(isNullOrEmpty(""));
    }

    @Test
    void isQuestionValidTest(){
        assertTrue(isQuestionValid(question));
        assertFalse(isQuestionValid(new Question("", "1", "2", "3")));
    }




}
