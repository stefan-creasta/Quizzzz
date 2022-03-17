package server.api;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import commons.Game;
import commons.PlayerAnswer;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.QuestionRepository;
import server.service.GameService;
import server.service.QuestionService;


@RestController
@RequestMapping("/api/answer")
public class AnswerController {


    private QuestionService questionService;
    private GameService gameService;

    private static Gson gson = new Gson();

    @Autowired
    public AnswerController(QuestionService questionService, GameService gameService) {
        this.questionService = questionService;
        this.gameService = gameService;
    }

    @PostMapping("")
    public void postAnswer(@RequestBody String item) {
        System.out.println(item);

        PlayerAnswer ans =  gson.fromJson(item, new TypeToken<PlayerAnswer>(){}.getType());

        Game g = gameService.getId(ans.gameId);

        Question q = questionService.getId(g.questions.get(g.currentQuestion).id);

        System.out.println(q);

        if(ans.answer.equals(q.answer)){
            System.out.println("true answer");
        }else{
            System.out.println("wrong answer");
        }
    }


}
