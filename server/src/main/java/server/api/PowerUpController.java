package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import server.service.GameService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/powerup")
public class PowerUpController {

    GameService service;

    @Autowired
    /**
     * Constructor
     */
    public PowerUpController(GameService service) {
        this.service = service;
    }


    @GetMapping("/{message}")
    /**
     * Takes the get request from the client with an appended message holding the
     * type of power up, the playerID and the gameID. Depending on the type
     * of power up, the respective power up method is called with the playerID
     * and the gameID. returns to the client a message that tells whether the
     * power up has been activated, together with other important information.
     */
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
