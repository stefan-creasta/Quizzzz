package server.service;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.QuestionRepository;

import java.util.List;


@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
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



}

