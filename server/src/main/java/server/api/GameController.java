package server.api;

import commons.Game;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.service.GameService;
import server.service.QuestionService;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final GameService service;
    private final QuestionService questionService;

    @Autowired
    public GameController(GameService service, QuestionService questionService) {
        this.service = service;
        this.questionService = questionService;
    }

    @GetMapping("/{id}")
    public Question getById(@PathVariable("id") long id) {
        if (id < 0 || !service.existsById(id)) {
            return null;
        }
        Game g = service.getId(id);
        return questionService.getId(g.getCurrentQuestion());
    }

    @PostMapping("/create")
    public long createGame(){
        return service.startGame();
    }
}
