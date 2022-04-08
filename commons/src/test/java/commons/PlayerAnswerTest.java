package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerAnswerTest {

    @Test
    void testConstructor() {
        PlayerAnswer pa = new PlayerAnswer("a", 0, 0);
        assertNotNull(pa);
    }

    @Test
    void testToString() {
        PlayerAnswer pa = mock(PlayerAnswer.class);
        when(pa.toString()).thenReturn("abcd");
        assertEquals("abcd", pa.toString());
    }
}