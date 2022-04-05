package server.api;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;
import server.service.LeaderboardService;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaderboardControllerTest {
    LeaderboardService service = mock(LeaderboardService.class);
    LeaderboardController controller = new LeaderboardController(service);
    Date d = new Date();
    List<LeaderboardEntry> leaderboardEntries = List.of(
            new LeaderboardEntry("Tina", 5000, d),
            new LeaderboardEntry("Tony", 2500, d)
    );

//    @Test
//    public void postLeaderboardTest() {
//        controller.postLeaderboard(leaderboardEntries.get(0));
//    }

    @Test
    public void getLeaderboardTest() {
        when(service.getLeaderboard(10)).thenReturn(leaderboardEntries);

        List<LeaderboardEntry> returned = controller.getLeaderboard();

        assertEquals(leaderboardEntries, returned);
    }
}
