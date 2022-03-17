package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import server.service.QuestionService;

@Component
public class QuestionsImporter implements ApplicationRunner {
    public static final String optionName = "activitiesSource";
    private QuestionService service;

    private Logger logger = LoggerFactory.getLogger(QuestionsImporter.class);

    public QuestionsImporter(QuestionService service) {
        this.service = service;
    }
    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption(optionName)) {
            String activitiesPath = args.getOptionValues(optionName).get(0);
            logger.info("Detected the " + optionName + " application option. Importing the activities from " + activitiesPath);
            importQuestions(activitiesPath);
        }
        else {
            logger.info("Could no detect the " + optionName + " application option. Not importing any activities.");
        }
    }

    public void importQuestions(String activitiesPath) {

    }
}

