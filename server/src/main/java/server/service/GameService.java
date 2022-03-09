package server.service;

import commons.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import server.database.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final QuestionService questionService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       @Qualifier("questionService") QuestionService questionService){
        this.gameRepository = gameRepository;
        this.questionService = questionService;
    }

    public Game getId(long id){
        Game g = gameRepository.getId(id);
        return g;
    }

    public long startGame(){
        String l = questionService.quizList();
        Game g = new Game(l);
        gameRepository.save(g);
        System.out.println(g.quiz);
        return g.id;
    }

    public boolean existsById(long id){
        return gameRepository.existsById(id);
    }
}
