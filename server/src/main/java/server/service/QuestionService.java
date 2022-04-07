package server.service;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.QuestionRepository;

import java.util.*;


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    /**
     * Function that returns a list of 20 random question.
     * It makes a list of 20 random questions that are of different types
     * (5 question of each type, in order 0, 1, 2, 3 and repeat)
     * @return
     */
    public List<Question> getRandom(){
        List<Question> all = questionRepository.getAll();
        List<Question> ret = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            for (int type = 0; type < 4; type++) {
                Question q = all.get(random.nextInt(all.size()));
                while (!q.type.equals(String.valueOf(type))) {
                    q = all.get(random.nextInt(all.size()));
                }
                ret.add(q);
            }
        }
        return ret;
    }

    public Question addNewQuestion(Question question){
        return questionRepository.save(setMissingAnswers(question));
    }

    public boolean existsById(long id){
        return questionRepository.existsById(id);
    }

    public Question getId(long id){
        return questionRepository.getId(id);
    }

    public List<Question> getAll(){
        return questionRepository.getAll();
    }

    public void deleteAll() {
        questionRepository.deleteAll();
    }

    /**
     * Searches for open-ended and watts answer multiple choice questions.
     * whose question title match the given search keywords.
     * @param q the search keywords
     * @return the list of results
     */
    public List<Question> search(String q) {
        System.out.println("Searching questions for " + q);
        return questionRepository.searchWithWattsAnswer("%" + q + "%");
    }

    /**
     * If the question type is watts answer multiple choice, sets the wrong answers to proper values.
     * @param q
     * @return
     */
    public Question setMissingAnswers(Question q) {
        if (q.type.equals("1")) {
            final List<Double> factors = new LinkedList<>(List.of(.1, .2, .25, .33, .4, .5, .66, .75, 1.25, 1.5, 2., 2.5, 3., 4., 5., 10.));
            final List<Double> factorsSimilar = new LinkedList<>(List.of(.7, .8, .9, 1.1, 1.2, 1.3));

            List<Double> chosenFactors = factors;

            int randomIndex = new Random().nextInt(2);

            if(randomIndex==1){
                chosenFactors = factorsSimilar;
            }
            Collections.shuffle(chosenFactors);

            if (q.wrongAnswer1 == null)
                q.wrongAnswer1 = String.format("%.0f", chosenFactors.get(0) * Double.parseDouble(q.answer));
            if (q.wrongAnswer2 == null)
                q.wrongAnswer2 = String.format("%.0f", chosenFactors.get(1) * Double.parseDouble(q.answer));
        }
        return q;
    }

    /**
     * Patches the question on the database with the same id with the provided question.
     * @param question
     * @return
     */
    public Question patchQuestion(Question question) {
        if (questionRepository.existsById(question.id)) {
            Question old = questionRepository.getId(question.id);
            if (question.question == null) question.question = old.question;
            if (question.type == null) question.type = old.type;
            if (question.answer == null) {
                //keep old answers.
                question.answer = old.answer;
                question.wrongAnswer1 = old.wrongAnswer1;
                question.wrongAnswer2 = old.wrongAnswer2;
            }
            else {
                //keep new answers but also generate the wrong answers.
                setMissingAnswers(question);
            }
            if (question.questionImage == null) question.questionImage = old.questionImage;
            return question;
        }
        else return null;
    }

    /**
     * Deletes the question with the provided id from the database.
     * @param id
     */
    public void deleteQuestion(long id) {
        questionRepository.deleteById(id);
    }
}

