package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import server.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/powerup")
public class PowerUpController {

    //postmapping: string being given as argument, string contains: playerID, powerUp name
    //if power up is double points - we turn on a flag on the player's object instance,

    GameService service;

    @Autowired
    public PowerUpController(GameService service) {
        this.service = service;
    }


    @GetMapping("/{message}")
    public String postPowerUp(@PathVariable("message") String message){
        long gameID = Long.parseLong(message.split("___")[2]);
        long playerID = Long.parseLong(message.split("___")[1]);
        switch(message.split("___")[0]){

            case "doublePointsPowerUp":
                return service.doublePointsPowerUp(playerID, gameID);

            case "eliminateWrongAnswerPowerUp":
                return service.eliminateWrongAnswerPowerUp(playerID, gameID);

            case "halfTimePowerUp":
                return service.halfTimePowerUp(playerID, gameID);
        }
        return null;
    }

}
