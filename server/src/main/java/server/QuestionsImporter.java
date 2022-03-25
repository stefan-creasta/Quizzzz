package server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import server.service.QuestionService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class QuestionsImporter implements ApplicationRunner {
    static long questionIdGenerator = 0;
    public static class Activity {
        @JsonIgnore
        final List<Double> factors = new LinkedList<>(List.of(.25, .5, 2., 3.));
        public String id;
        public double consumption_in_wh;
        public String image_path;
        public String title;
        public String source;

        public Activity() {};

        Question toQuestion(URI imageURIRoot, QuestionService service) throws MalformedURLException, UnsupportedEncodingException {
            long id;

            //TODO: Check if an activity with the JSON id exists in the database
            if (false) {
                //TODO: If so, get its already existing id here so we can use it and not risk duplicate entries.
            }
            else {
                while (service.existsById(questionIdGenerator))
                    questionIdGenerator++;
                id = questionIdGenerator;
            }
            Collections.shuffle(factors);

            String answer = String.format("%.0f", consumption_in_wh);
            String wrongAnswer1 = String.format("%.0f", factors.get(0) * consumption_in_wh);
            String wrongAnswer2 = String.format("%.0f", factors.get(1) * consumption_in_wh);
            var imageRelativeURI = URI.create(image_path.replace(" ", "%20"));
            String imageURL = imageURIRoot.resolve(imageRelativeURI).toURL().toString().replace("http://localhost:8080/images/", "images/");
            Question q = new Question(id, title,answer,wrongAnswer1,wrongAnswer2);
            q.questionImage = imageURL;
            return q;
        }
    }

    public static final String optionName = "activitiesSource";
    private final QuestionService service;

    private Logger logger = LoggerFactory.getLogger(QuestionsImporter.class);

    public QuestionsImporter(QuestionService service) {
        this.service = service;
    }
    @Override
    public void run(ApplicationArguments args) throws IOException {

        if (args.containsOption(optionName)) {
            String optionValue = args.getOptionValues(optionName).get(0);
            logger.info("Detected the " + optionName + " application option. Importing the activities/questions from " + optionValue);
            //service.resetId();
            try {
                importQuestions(optionValue);
            }
            catch (IllegalArgumentException e) {
                logger.error("The activities source could not be processed. The path should be in form a/b/c, not in form ./a or b/");
            }
        }
        else {
            logger.info("Could not detect the " + optionName + " application option. Not importing any activities/questions.");
        }
    }

    /**imports activities from the given path
     * Note that the path is expected to be under server/resources/images. If not so, loading the question images will
     * fail because the only public path being hosted is the server/resources/images for security reasons.
     * @param path the path relative to server/resources/images
     * @throws IOException
     */
    public void importQuestions(String path) throws IOException {
        if (path.startsWith("/") || path.endsWith("/") || path.startsWith("."))
            throw new IllegalArgumentException();
        questionIdGenerator = 0;
        ObjectMapper mapper = new ObjectMapper();
        List<Activity> activities = mapper.readValue(
            Paths.get("server/resources/images", path, "activities.json").toUri().toURL(),
            new TypeReference<>() {}
        );
        final URI imageURIRoot = URI.create("http://localhost:8080/images/" + path + "/");
        final List<String> malformed = new LinkedList<>();
        service.deleteAll();
        activities.stream().map(x -> {
            try {
                return x.toQuestion(imageURIRoot, service);
            } catch (MalformedURLException e) {
                malformed.add(x.id);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).forEach(service::addNewQuestion);
        if (!malformed.isEmpty()) {
            logger.warn("MalformedURLExceptions have happened with activities " + String.join(", ", malformed));
        }
    }
}

