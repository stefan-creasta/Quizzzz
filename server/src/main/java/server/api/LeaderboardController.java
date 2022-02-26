package server.api;

import commons.LeaderboardEntry;
import org.springframework.web.bind.annotation.*;
import server.service.LeaderboardService;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    public LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService ls) {
        leaderboardService = ls;
    }

    @GetMapping("")
    public List<LeaderboardEntry> getLeaderboard() {
        return leaderboardService.getLeaderboard(-1);
    }

    @PostMapping("")
    public void postLeaderboard(@RequestBody LeaderboardEntry item) {
        leaderboardService.addEntry(item);
    }
}
