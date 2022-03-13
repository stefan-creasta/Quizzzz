package server.database;


import commons.PowerUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PowerUpRepository extends JpaRepository<PowerUp,Long> {
    @Query("SELECT pu FROM PowerUp pu")
    List<PowerUp> getPowerUpList();
}
