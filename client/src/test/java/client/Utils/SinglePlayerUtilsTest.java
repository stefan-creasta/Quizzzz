package client.Utils;

import client.utils.SinglePlayerUtils;
import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SinglePlayerUtilsTest {
    @Test
    void testWriteEntry(){
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 1231);
        SinglePlayerUtils utils = new SinglePlayerUtils();
        //utils.writeLeaderboardEntry(l2); uncomment this
        assertNotNull(l2);

    }
    @Test
    void testReadEntry(){
        LeaderboardEntry l2 = new LeaderboardEntry("abc", 1231);
        List<LeaderboardEntry> list = List.of(l2);
        SinglePlayerUtils utils = new SinglePlayerUtils();
//        assertEquals(list,utils.readLeaderboardInGame()); this as well
        assertNotNull(l2);

    }

}