package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, String> {
    @Query ("SELECT lb.username, lb.score FROM LeaderboardEntry lb ORDER BY lb.score DESC, lb.date ASC NULLS LAST")
    List<LeaderboardEntry> findTopN(Pageable pageable);

    @Query ("SELECT lb.username, lb.score FROM LeaderboardEntry lb ORDER BY lb.score DESC, lb.date ASC NULLS LAST")
    List<LeaderboardEntry> findAllSorted();
}
