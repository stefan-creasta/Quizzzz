package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PowerUpTest {

    @Test
    void testConstructor() {
        PowerUp p = new PowerUp("abc", "1");
        assertNotNull(p);
    }

    @Test
    void testEquals1() {
        PowerUp p = new PowerUp("abc", "1");
        assertEquals(p, p);
    }

    @Test
    void testEquals2() {
        PowerUp p1 = new PowerUp("abc", "1");
        PowerUp p2 = new PowerUp("abc", "2");
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        PowerUp p = new PowerUp("abc", "1");
        assertEquals(959268, p.hashCode());
    }

    @Test
    void testToString() {
        PowerUp p = mock(PowerUp.class);
        when(p.toString()).thenReturn("abcd");
        assertEquals(p.toString(), "abcd");
    }
}