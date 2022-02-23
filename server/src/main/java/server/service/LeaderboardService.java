package server.service;

import commons.LeaderboardEntry;
import org.springframework.stereotype.Service;
import server.database.LeaderboardRepository;

import java.util.List;
import java.util.stream.Collectors;

/**the service class to manage the global leaderboard and its database
 */
@Service
public class LeaderboardService {
    public final LeaderboardRepository repo;

    public LeaderboardService(LeaderboardRepository repo) {
        this.repo = repo;
    }

    /**gets the current leaderboard up to a max rank
     * negative max rank will yield no dropping
     * @param maxRank the maximum rank
     * @return the current leaderboard
     */
    public List<LeaderboardEntry> getLeaderboard(int maxRank) {
        if (maxRank < 0) {
            return repo.findAll().stream().sorted().collect(Collectors.toList());
        }
        else {
            return repo.findAll().stream().sorted().limit(maxRank).collect(Collectors.toList());
        }
    }

    /**adds/updates the leaderboard item corresponding to the new leaderboard entry
     * @param newEntry the new leaderboard item
     */
    public synchronized void addEntry(LeaderboardEntry newEntry) {
        repo.save(newEntry);
    }

    /**sorts the leaderboard as stored in the database and drops the ranks lower than the given max rank
     * negative max rank will yield no dropping
     * @param maxRank the maximum rank
     */
    public synchronized void orderAndCutLeaderboard(int maxRank) {
        if (maxRank < 0) {
            repo.saveAll(repo.findAll().stream().sorted().collect(Collectors.toList()));
        }
        else {
            repo.saveAll(repo.findAll().stream().sorted().limit(maxRank).collect(Collectors.toList()));
        }
    }
}
