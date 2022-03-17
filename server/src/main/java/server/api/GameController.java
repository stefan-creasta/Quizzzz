package server.api;

import commons.Game;
import commons.GameState;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.service.GameService;

@RestController
@RequestMapping("api/game")
public class GameController {

    private final GameService service;

    @Autowired
    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Question getById(@PathVariable("id") long id) {
        if (id < 0 || !service.existsById(id)) {
            return null;
        }
        Game g = service.getId(id);
        return g.getCurrentQuestion();
    }

    @PostMapping("/create")
    public long createGame(){
        return service.createGame();
    }

    @GetMapping("/join/{id}")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public GameState joinGame(@PathVariable long id, @RequestParam String username) throws IllegalArgumentException {
        return service.joinGame(id, username);
    }

    @PostMapping("/init/{id}")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public void initiateGame(@PathVariable long id) throws IllegalArgumentException{
        service.initiateGame(id);
    }
}
