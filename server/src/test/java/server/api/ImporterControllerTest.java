package server.api;

import org.junit.jupiter.api.Test;
import server.QuestionsImporter;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ImporterControllerTest {

    QuestionsImporter questionsImporter = mock(QuestionsImporter.class);
    ImporterController importerController = new ImporterController(questionsImporter);
    @Test
    public void importerControllerTest() throws IOException {
        assertEquals("Activities have been imported successfully from activities-bank",importerController.importQuestions("activities-bank"));
        verify(questionsImporter).importQuestions("activities-bank");

    }


}