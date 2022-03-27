package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.QuestionsImporter;

@RestController
@RequestMapping("/api/import")
public class ImporterController {
    private QuestionsImporter importer;

    public ImporterController(QuestionsImporter importer) {
        this.importer = importer;
    }

    @GetMapping("")
    public String importQuestions(@RequestParam String activitiesSource) {
        try {
            importer.importQuestions(activitiesSource);
            return "Activities have been imported successfully from " + activitiesSource;
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}
