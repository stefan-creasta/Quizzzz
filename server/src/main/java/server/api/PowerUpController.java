package server.api;

import commons.PowerUp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.PowerUpService;

import java.util.List;

@RestController
@RequestMapping("/api/powerup")
public class PowerUpController {
    public PowerUpService service;
    public PowerUpController(PowerUpService service){
            this.service = service;
    }
    @PostMapping("")
    public List<PowerUp> getPowerUp() {
        return service.getPowerUpList();
    }
}
