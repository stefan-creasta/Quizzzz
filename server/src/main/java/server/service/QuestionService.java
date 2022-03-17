package server.service;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.QuestionRepository;

import java.util.Random;


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }

    public Question addNewQuestion(Question question){
        Question saved = questionRepository.save(question);
        return saved;
    }

    public boolean existsById(long id){
        return questionRepository.existsById(id);
    }

    public Question getId(long id){
        Question q = questionRepository.getId(id);
        return q;
    }

    public String quizList(){
        String l = "";
        Random random = new Random();
        for(int i=0; i<20 && i<questionRepository.count(); i++) {
            int idx = random.nextInt((int) questionRepository.count()) + 1;
            while(l.contains(String.valueOf(idx)))
                idx = random.nextInt((int) questionRepository.count());
            l += (char) idx;
        }
        return l;
    }

    public void deleteAll() {
        questionRepository.deleteAll();
    }

}

