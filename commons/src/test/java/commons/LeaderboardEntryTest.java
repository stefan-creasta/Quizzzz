package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryTest {
    @Test
    void compareToEqual() {
        Date d = mock(Date.class);
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 5, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 5, d);
        when(d.compareTo(d)).thenReturn(5);
        assertTrue(5 == l1.compareTo(l2));
    }
    @Test
    void compareToNotEqual() {
        Date d = mock(Date.class);
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 2, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 1, d);
        assertTrue(-1 == l1.compareTo(l2));
    }

    @Test
    void testEquals() {
        Date d = new Date(3, 3, 3);
        //when(d.equals(d)).thenReturn(true);
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 2, d);
        LeaderboardEntry l2 = new LeaderboardEntry("cbd", 1, d);
        LeaderboardEntry l3 = new LeaderboardEntry("abc", 1, d);
        assertTrue(l1.equals(l1));
        assertFalse(l1.equals(l2));
        assertFalse(l1.equals(l3));
    }

    @Test
    void testHashCode() {
        Date d = new Date(3, 3, 3);
        LeaderboardEntry l1 = new LeaderboardEntry("abc", 2, d);
        System.out.println(l1.hashCode());
        assertEquals(-72400922, l1.hashCode());
    }

    @Test
    void testToString() {
        Date d = new Date(3, 3, 3);
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 2, d);
        //System.out.println(l1.toString());
        //LeaderboardEntry l1 = mock(LeaderboardEntry.class);
        //when(ToStringBuilder.reflectionToString(any(LeaderboardEntry.class), MULTI_LINE_STYLE)).thenReturn("a");
        assertEquals("commons.LeaderboardEntry@33a053d[\r\n" +
                "  date=Fri Apr 03 00:00:00 EET 1903\r\n" +
                "  score=2\r\n" +
                "  username=abc\r\n" +
                "]", l2.toString());
    }
}