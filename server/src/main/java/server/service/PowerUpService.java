package server.service;

import server.database.PowerUpRepository;
import commons.PowerUp;

public class PowerUpService {
    public final PowerUpRepository repo;
    public PowerUpService(PowerUpRepository repo){
        this.repo=repo;

    }
    public PowerUp getID(long ID) {
            PowerUp p = repo.getId(ID);
            return p;
    }
}
