package server.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/powerup")
public class PowerUpController {
    public PowerUpController(){

    }
    @PostMapping("")
    public void postPowerUp(@RequestBody String item) {
        System.out.println(item + " server");
    }
}
