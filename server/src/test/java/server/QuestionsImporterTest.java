package server;

import commons.Question;
import org.junit.jupiter.api.Test;
import server.service.QuestionService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionsImporterTest {
    @Test
    public void importQuestionsBadPathTest() {
        List<String> badPaths = List.of(
                "/activities",
                "activities/",
                "./activities",
                "./activities/"
        );

        QuestionService service = mock(QuestionService.class);
        when(service.addNewQuestion(any(Question.class))).thenReturn(null);

        final QuestionsImporter importer = new QuestionsImporter(service);
        for (final String path : badPaths) assertThrows(IllegalArgumentException.class, () -> importer.importQuestions(path));
    }

    @Test
    public void importQuestionsTest() throws IOException {
        List<Question> collection = new LinkedList<>();

        QuestionService service = mock(QuestionService.class);
        when(service.addNewQuestion(any(Question.class))).thenAnswer(I -> {
            Question q = I.getArgument(0);
            q.id = 0;
            collection.add(q);
            return q;
        });

        final QuestionsImporter importer = new QuestionsImporter(service);

        importer.importQuestions("activity-bank-test");

        List<String> specQuestions = List.of(
                "Using a hairdryer for an hour",
                "Using a leafblower for 15 minutes",
                "Making a hot cup of coffee"
        );

        List<String> specAnswers = List.of("1200", "183", "300");

        for (int i = 0; i < 3; i++) {
            assertEquals(specQuestions.get(i), collection.get(i).question);
            assertEquals(specAnswers.get(i), collection.get(i).answer);
            assertNotNull(collection.get(i).wrongAnswer1);
            assertNotNull(collection.get(i).wrongAnswer2);
        }
    }
}