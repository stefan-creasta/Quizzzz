package server.database;


import commons.PowerUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PowerUpRepository extends JpaRepository<PowerUp,Long> {
    @Query("SELECT pu FROM PowerUp pu WHERE pu.id = :id")
    public PowerUp getId(long id);
}
