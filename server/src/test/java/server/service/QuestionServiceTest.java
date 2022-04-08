package server.service;

import commons.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.QuestionRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    private final QuestionRepository questionRepository = mock(QuestionRepository.class);

    private QuestionService questionService;

    Question question0 = new Question(0, "question", "answer", "wa1", "wa2", "0");
    Question question1 = new Question(1, "question", "answer", "wa1", "wa2", "1");
    Question question2 = new Question(2, "question", "answer", "wa1", "wa2", "2");
    Question question3 = new Question(3, "question", "answer", "wa1", "wa2", "3");

    @BeforeEach
    void setUp() {
        questionService = new QuestionService(questionRepository);
        when(questionRepository.existsById(anyLong())).thenReturn(true);
        when(questionRepository.getId(anyLong())).thenReturn(question0);
        when(questionRepository.getAll()).thenReturn(List.of(question0, question1, question2, question3));
        when(questionRepository.save(question0)).thenReturn(question0);
        when(questionRepository.searchWithWattsAnswer("answer")).thenReturn(List.of(question0, question1, question2, question3));
        when(questionRepository.searchWithWattsAnswer("%question%")).thenReturn(List.of(question0, question1, question2, question3));
    }

    @Test
    void getRandom() {
        List<Question> expected = List.of(question0, question1, question2, question3,
                question0, question1, question2, question3,
                question0, question1, question2, question3,
                question0, question1, question2, question3,
                question0, question1, question2, question3);
        List<Question> questions = questionService.getRandom();
        assertEquals(expected, questions);
    }

    @Test
    void addNewQuestion() {
        assertEquals(question0, questionService.addNewQuestion(question0));
    }

    @Test
    void existsById() {
        assertTrue(questionService.existsById(0));
    }

    @Test
    void getId() {
        assertEquals(question0, questionService.getId(0));
    }

    @Test
    void getAll() {
        assertEquals(List.of(question0, question1, question2, question3), questionService.getAll());
    }

    @Test
    void deleteAll() {
        questionService.deleteAll();
        verify(questionRepository, times(1)).deleteAll();
    }

    @Test
    void search() {
        assertEquals(List.of(question0, question1, question2, question3), questionService.search("question"));
        verify(questionRepository, times(1)).searchWithWattsAnswer("%question%");
    }

    @Test
    void setMissingAnswers() {
        assertEquals(question0, questionService.setMissingAnswers(question0));
    }

    @Test
    void patchQuestion() {
        assertEquals(question0, questionService.patchQuestion(question0));
    }

    @Test
    void deleteQuestion() {
        questionService.deleteQuestion(0);
        verify(questionRepository, times(1)).deleteById((long)0);
    }
}