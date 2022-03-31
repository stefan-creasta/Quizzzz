package server.api;

import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.QuestionRepository;
import server.service.QuestionService;


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


}
