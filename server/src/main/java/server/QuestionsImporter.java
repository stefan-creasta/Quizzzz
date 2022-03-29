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
import java.util.*;

@Component
public class QuestionsImporter implements ApplicationRunner {
    static long questionIdGenerator = 0;

    public List<Activity> activities = null;

    public static class Activity {
        @JsonIgnore
        final List<Double> factors = new LinkedList<>(List.of(.25, .5, 2., 3.));
        public String id;
        public double consumption_in_wh;
        public String image_path;
        public String title;
        public String source;
        public Random rand = new Random();

        public Activity() {};

        Question toQuestion(URI imageURIRoot, QuestionService service, List<Activity> activities) throws MalformedURLException, UnsupportedEncodingException {
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

            int qtype = rand.nextInt(2);
            Question q = null;
            if (qtype == 0) { // Case for 'Instead of ..., you could ...'
                Activity b = getEqualEnergy(this, activities);
                if (this.id.equals(b.id))
                    qtype = 1;
                else {
                    String answer = b.title;
                    b = getDiffEnergy(this, activities);
                    String wrongAnswer1 = b.title;
                    Activity c = getDiffEnergy(this, activities);
                    while (c.id.equals(b.id))
                        c = getDiffEnergy(this, activities);
                    String wrongAnswer2 = c.title;
                    var imageRelativeURI = URI.create(image_path.replace(" ", "%20"));
                    String imageURL = imageURIRoot.resolve(imageRelativeURI).toURL().toString().replace("http://localhost:8080/images/", "images/");
                    q = new Question(id, "Instead of '" + title + "', you could:", answer, wrongAnswer1, wrongAnswer2);
                    q.questionImage = imageURL;
                }
            }
            if (qtype == 1) { // Case for 'How much energy does it take to ...?'
                String answer = String.format("%.0f", consumption_in_wh);
                String wrongAnswer1 = String.format("%.0f", factors.get(rand.nextInt(4)) * consumption_in_wh);
                String wrongAnswer2 = String.format("%.0f", factors.get(rand.nextInt(4)) * consumption_in_wh);
                var imageRelativeURI = URI.create(image_path.replace(" ", "%20"));
                String imageURL = imageURIRoot.resolve(imageRelativeURI).toURL().toString().replace("http://localhost:8080/images/", "images/");
                q = new Question(id, title,answer,wrongAnswer1,wrongAnswer2);
                q.questionImage = imageURL;
            }
            if (qtype == 2) {

            }
            return q;
        }

        /**
         * Function that returns an activity with the same energy consumption as a given one.
         * @param a Activity for which we want to find an equal.
         * @return Activity with the same energy consumption as activity a.
         */
        public Activity getEqualEnergy(Activity a, List<Activity> activities) {
            Random random = new Random();
            Activity b = activities.get(random.nextInt(activities.size()));
            int count = 0;
            while ((a.id.equals(b.id) || a.consumption_in_wh != b.consumption_in_wh) && count<200) {
                b = activities.get(random.nextInt(activities.size()));
                count++;
            }
            if (count == 200)
                return a;
            return b;
        }

        /**
         * Function that returns an activity with different energy consumption than a given one.
         * @param a Activity for which we want to find a random different activity.
         * @return Activity with a different energy consumption than activity a.
         */
        public Activity getDiffEnergy(Activity a, List<Activity> activities) {
            Random random = new Random();
            Activity b = activities.get(random.nextInt(activities.size()));
            while (a.consumption_in_wh == b.consumption_in_wh)
                b = activities.get(random.nextInt(activities.size()));
            return b;
        }
    }

    public static final String optionName = "activitiesSource";
    private final QuestionService service;

    private Logger logger = LoggerFactory.getLogger(QuestionsImporter.class);

    public QuestionsImporter(QuestionService service) {
        this.service = service;
    }

    /**
     * Run as soon as the application starts and all the services, controllers are initialized.
     *
     * It reads the path relative to server/resources/images from the application option activitiesSource. If no
     * such option is present no importing is done.
     * @param args the Spring application arguments
     * @throws IOException
     */
    @Override
    public void run(ApplicationArguments args) throws IOException {

        if (args.containsOption(optionName)) {
            String optionValue = args.getOptionValues(optionName).get(0);
            logger.info("Detected the " + optionName + " application option. Importing the activities/questions from " + optionValue);
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

    /**
     * Imports activities from the given path.
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
        activities = mapper.readValue(
            Paths.get("server/resources/images", path, "activities.json").toUri().toURL(),
            new TypeReference<>() {}
        );
        final URI imageURIRoot = URI.create("http://localhost:8080/images/" + path + "/");
        final List<String> malformed = new LinkedList<>();
        service.deleteAll();
        activities.stream().map(x -> {
            try {
                return x.toQuestion(imageURIRoot, service, activities);
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

