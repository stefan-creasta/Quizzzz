package server.api;

import commons.GameState;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.GameService;

@RestController
@RequestMapping("/api/listen")
public class ListenController {
    GameService service;

    public ListenController(GameService service) {
        this.service = service;
    }

    @GetMapping("")
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public DeferredResult<GameState> getListen(@RequestParam Long playerId) throws IllegalArgumentException {
        if (playerId == null) throw new IllegalArgumentException();
        final DeferredResult<GameState> result = new DeferredResult<>();
        service.registerPlayerConnection(playerId, result);
        return result;
    }
}
