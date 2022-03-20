package server.service;
import org.springframework.stereotype.Service;
import server.database.PowerUpRepository;
import commons.PowerUp;

import java.util.List;
@Service
public class PowerUpService {
    public final PowerUpRepository repo;
    public PowerUpService(PowerUpRepository repo){
        this.repo=repo;

    }
    public List<PowerUp> getPowerUpList() {
        return repo.getPowerUpList();
    }
    public PowerUp addNewPowerUp(PowerUp p){
        return repo.save(p);
    }
}
