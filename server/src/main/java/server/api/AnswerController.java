package server.api;


import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/answer")
public class AnswerController {


    public AnswerController() {
    }

    @PostMapping("")
    public void postAnswer(@RequestBody String item) {
        System.out.println(item + " server");
    }
}
