package server.service;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.QuestionRepository;


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

}

