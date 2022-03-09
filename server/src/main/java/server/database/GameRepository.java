package server.database;

import commons.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g WHERE g.id = :id")
    public Game getId(long id);
}