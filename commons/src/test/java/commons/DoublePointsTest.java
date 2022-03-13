package commons;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DoublePointsTest {

    @Test
    void TestEquals(){
        Timer timer = new Timer(0,20);
        Timer timer2 = new Timer(0,19);
        String time = timer.toString();
        String time2 = timer2.toString();
        DoublePoints a = new DoublePoints("username",time);
        DoublePoints b = new DoublePoints("username",time);
        DoublePoints c = new DoublePoints("talkingbenthedog",time2);
        assertTrue(a.equals(b));
        assertFalse(b.equals(c));
        assertFalse(a.equals(c));
    }
    @Test
    void TestHashCode(){
        Timer timer = new Timer(0,20);
        String time = timer.toString();
        DoublePoints a = new DoublePoints("username",time);
        DoublePoints b = new DoublePoints("username",time);
        assertEquals(a.hashCode(),b.hashCode());

    }
    @Test
    void TestToString(){
        Timer timer = new Timer(0,20);
        String time = timer.toString();
        DoublePoints a = new DoublePoints("username",time);
        assertLinesMatch(List.of(
                "commons.DoublePoints@[0-9a-f]*\\[",
                "  id=0",
                "  time="+time,
                "  username=username",
                "]"
        ), Arrays.asList(a.toString().split(System.lineSeparator())));


    }
}