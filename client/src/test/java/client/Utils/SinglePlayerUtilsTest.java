package client.Utils;

import client.utils.SinglePlayerUtils;
import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SinglePlayerUtilsTest {
    @Test
    void testWriteEntry(){
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 1231);
        LeaderboardEntry l3 = new LeaderboardEntry("abcd", 11231);
        SinglePlayerUtils utils = new SinglePlayerUtils();
        utils.writeLeaderboardEntry(l2);
        utils.writeLeaderboardEntry(l3);
        assertNotNull(l2);

    }

    /**
     * Reads from the text file that was generated the writer
     * every time it is called, failing the test
     */
    @Test
    void testReadEntry(){
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 1231);
        LeaderboardEntry l3 = new LeaderboardEntry("abcd", 11231);
        List<LeaderboardEntry> list = new ArrayList<>();
        list.add(l2);
        list.add(l3);
        SinglePlayerUtils utils = new SinglePlayerUtils();
        List<LeaderboardEntry> test = utils.readLeaderboardInGame();
        assertEquals(list.get(1).toString(),test.get(1).toString());

    }

}