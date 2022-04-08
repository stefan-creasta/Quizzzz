package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testConstructor1() {
        Player p = new Player();
        assertNotNull(p);
    }

    @Test
    void testConstructor2() {
        Player p = new Player("abc", 0);
        assertNotNull(p);
    }
}