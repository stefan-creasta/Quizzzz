package server;

import commons.Question;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import server.service.QuestionService;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static server.QuestionsImporter.Activity;

class QuestionsImporterTest {
    @Test
    public void runTestNoImport() throws IOException {
        QuestionService service = mock(QuestionService.class);
        when(service.addNewQuestion(any(Question.class))).thenAnswer(I -> {
            Question q = I.getArgument(0);
            q.id = 0;
            return q;
        });

        QuestionsImporter importer = new QuestionsImporter(service);

        ApplicationArguments args = mock(ApplicationArguments.class);
        when(args.containsOption(eq("activitiesSource"))).thenReturn(false);
        importer.run(args);
    }

    @Test
    public void runTestImport() throws IOException {
        QuestionService service = mock(QuestionService.class);
        when(service.addNewQuestion(any(Question.class))).thenAnswer(I -> {
            Question q = I.getArgument(0);
            q.id = 0;
            return q;
        });

        QuestionsImporter importer = new QuestionsImporter(service);

        ApplicationArguments args = mock(ApplicationArguments.class);
        when(args.containsOption(eq("activitiesSource"))).thenReturn(true);
        when(args.getOptionValues(eq("activitiesSource"))).thenReturn(List.of("activity-bank-test"));
        importer.run(args);
    }

    @Test
    public void getEqualEnergySameTest() {
        Activity a = new Activity();
        a.id = "ex_a";
        a.consumption_in_wh = 1000.;

        List<Activity> activities = List.of(a, a);

        Activity equal = a.getEqualEnergy(a, activities);
        assertSame(a, equal);
    }

    @Test
    public void getEqualEnergyDifferentTest() {
        Activity a = new Activity();
        a.id = "ex_a";
        a.consumption_in_wh = 1000.;

        Activity b = new Activity();
        b.id = "ex_b";
        b.consumption_in_wh = 1000.;

        List<Activity> activities = List.of(a, b);

        Activity equal = a.getEqualEnergy(a, activities);

        assertTrue(b == equal || a == equal);
    }

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

        List<String> specQuestions = List.of(
                "Using a hairdryer for an hour",
                "Using a leafblower for 15 minutes",
                "Making a hot cup of coffee"
        );

        List<String> specAnswers = List.of("1200", "183", "300");

        String rootPath = "images/activity-bank-test/";
        List<String> specQuestionImages = List.of(
                rootPath + "38/hairdryer.png",
                rootPath + "38/leafblower.png",
                rootPath + "38/coffee.png"
        );

        System.out.println(collection);

        Set<String> encounteredQuestionTypes = new HashSet<>(4);
        while (encounteredQuestionTypes.size() < 3) {
            collection.removeIf(x -> true);
            importer.importQuestions("activity-bank-test");

            for (Question q : collection) {
                encounteredQuestionTypes.add(q.type);
                switch (q.type) {
                    //Instead of...
                    case "0":
                        boolean contains = false;
                        for (String searched : specQuestions) {
                            if (("Instead of '" + searched + "', you could:").equals(q.question)) {
                                contains = true;
                                break;
                            }
                        }
                        assertTrue(contains, "Instead of...:" + q.question + " is not known");
                        break;
                        //What requires more energy?
                    case "2":
                        assertEquals("What requires more energy?", q.question);
                        for (String a : List.of(q.answer, q.wrongAnswer1, q.wrongAnswer2)) {
                            assertTrue(specQuestions.contains(a));
                        }
                        break;
                        //Multiple choice
                    case "1":
                        assertNotNull(q.wrongAnswer1);
                        assertNotNull(q.wrongAnswer2);
                        //no break here
                        //Open-ended
                    case "3":
                        int len = "How much energy (in kW) does it take: '".length();
                        System.out.println(q.question);
                        assertEquals(specAnswers.get(specQuestions.indexOf(
                                q.question.substring(len, q.question.length() - 1)
                        )), q.answer);
                        break;
                }
                if (encounteredQuestionTypes.size() >= 4) break;
            }
        }
    }
}