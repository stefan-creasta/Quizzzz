package server.api;

import com.google.gson.Gson;
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
        return leaderboardService.getLeaderboard(10);
    }

    @PostMapping("")
    public void postLeaderboard(@RequestBody String item) {
//        var objectMapper = new ObjectMapper();
//        String requestBody = objectMapper
//                .writeValueAsString(entry);

        Gson g = new Gson();
        LeaderboardEntry p = g.fromJson(item, LeaderboardEntry.class);
        leaderboardService.addEntry(p);
    }
}
