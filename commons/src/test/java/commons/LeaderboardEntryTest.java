package commons;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryTest {
    @Test
    void compareToEqual() {
        Date d = new Date();

        LeaderboardEntry l1 = new LeaderboardEntry("abc", 5, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 5, d);

        assertEquals(0, l1.compareTo(l2));
    }

    @Test
    void compareToDifferentScores() {
        Date d = new Date();

        LeaderboardEntry l1 = new LeaderboardEntry("abc", 1, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 8, d);

        assertEquals(1, l1.compareTo(l2));
    }

    @Test
    void compareToDifferentDates() {
        Date d1 = new GregorianCalendar(1, 0, 1).getTime();
        Date d2 = new GregorianCalendar(1, 0, 2).getTime();

        LeaderboardEntry l1 = new LeaderboardEntry("abc", 1, d1);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 1, d2);

        assertEquals(d1.compareTo(d2), l1.compareTo(l2));
    }

    @Test
    void testEquals() {
        Date d = new Date(3, 3, 3);
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 2, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 1, d);
        LeaderboardEntry l3 = new LeaderboardEntry("abc", 1, d);
        assertTrue(l1.equals(l1));
        assertFalse(l1.equals(l2));
        assertFalse(l1.equals(l3));
    }

    @Test
    void testHashCode() {
        Date d = new GregorianCalendar(3, 3, 3).getTime();
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 2, d);
        l1.hashCode();
    }


    @Test
    void testToString() {
        Date d = new GregorianCalendar(3, 3, 3).getTime();
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 2, d);
        assertLinesMatch(List.of(
                "commons.LeaderboardEntry@[0-9a-f]*\\[",
                "  date=" + d,
                "  rank=-1",
                "  score=2",
                "  username=abc",
                "]"
        ), Arrays.asList(l2.toString().split(System.lineSeparator())));
    }
}