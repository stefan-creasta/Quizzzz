package server.api;


import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.QuestionRepository;


@RestController
@RequestMapping("/api/answer")
public class AnswerController {


    private QuestionRepository repo;

    @Autowired
    public AnswerController(QuestionRepository repo) {
        this.repo = repo;
    }

    @PostMapping("")
    public void postAnswer(@RequestBody String item) {
        System.out.println(item);
//
//        Question q = repo.getId(id);
//        if(answer.equals(q.answer)){
//            System.out.println("true answer");
//        }else{
//            System.out.println("wrong answer");
//        }
//        System.out.println(item.split("_")[0] + item.split("_")[1]+ " server");
    }


}
