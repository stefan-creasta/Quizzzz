package server.service;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.QuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
        return questionRepository.save(question);
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

}

