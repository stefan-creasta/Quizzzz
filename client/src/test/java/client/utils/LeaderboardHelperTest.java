package client.utils;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardHelperTest {

    @Test
    void testPrepareLeaderboard() {
        LeaderboardHelper helper = new LeaderboardHelper();

        List<LeaderboardEntry> testLeaderboard = new LinkedList<>();

        Date date = new GregorianCalendar(1, 1, 1).getTime();

        int score = 1500;
        for (int i = 0; i < 15; i++) {
            //A, B, C...
            testLeaderboard.add(new LeaderboardEntry(Character.toString((char) (i + 65)), score, date));
            score -= 100;
        }

        var result = helper.prepareLeaderboard(testLeaderboard, "G");

        var spec = new LinkedList<LeaderboardEntry>(List.of(
                new LeaderboardEntry("A", 1500, date),
                new LeaderboardEntry("B", 1400, date),
                new LeaderboardEntry("C", 1300, date),
                new LeaderboardEntry("D", 1200, date),
                new LeaderboardEntry("E", 1100, date),
                new LeaderboardEntry("G", 900, date)
        ));
        spec.add(5, null);
        assertEquals(spec, result);
    }
}