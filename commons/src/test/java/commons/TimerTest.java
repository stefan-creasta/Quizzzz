package commons;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    public boolean compareWithError(long a, long b, long error) {
        return Math.abs(a - b) < error;
    }

    @Test
    public void getDurationTest() {
        Timer timer = new Timer(3, 0);
        System.out.println(timer.getDuration().getNano());
        assertTrue(compareWithError(180, timer.getDuration().getSeconds(), 1));
    }

    @Test
    public void getDurationLongTest() {
        Timer timer = new Timer(3, 0);
        assertTrue(compareWithError(180_000_000, timer.getDurationLong(), 1_000_000));
    }

    @Test
    public void setDurationTest() {
        Timer timer = new Timer();
        timer.setDuration(Duration.ofMinutes(3).dividedBy(ChronoUnit.MICROS.getDuration()));
        assertTrue(compareWithError(180_000_000, timer.getDurationLong(), 1_000_000));
        timer.setDuration(3, 0);
        assertTrue(compareWithError(180_000_000, timer.getDurationLong(), 1_000_000));
        timer.setDuration(Duration.ofMinutes(3));
        assertTrue(compareWithError(180_000_000, timer.getDurationLong(), 1_000_000));
    }

    @Test
    public void getSynchronizationLongTest() {
        Timer timer = new Timer(3, 0);
        assertTrue(timer.getSynchronizationLong() < 1_000_000);
        timer.synchronize(10_000_000);
        assertTrue(compareWithError(10_000_000, timer.getSynchronizationLong(), 1_000_000));
    }

    @Test
    public void synchronizeTest() {
        Timer timer = new Timer();
        timer.setDuration(Duration.ofMinutes(3).dividedBy(ChronoUnit.MICROS.getDuration()));
        long secs = timer.synchronize(
                Duration.ofMinutes(2).dividedBy(ChronoUnit.MICROS.getDuration())
                ).getSeconds();
        System.out.println(secs);
        assertTrue(compareWithError(120, secs, 2));
    }

    @Test
    public void isElapsedTest() {
        Timer timer = new Timer(3, 0);
        timer.synchronize(Duration.ofSeconds(181).dividedBy(ChronoUnit.MICROS.getDuration()));
        assertTrue(timer.isElapsed());
    }

    @Test
    public void toTimerDisplayStringTest() {
        Timer timer = new Timer(3, 0);
        String result = timer.toTimerDisplayString();
        assertTrue(
                List.of("2:59", "3:00", "3:01").contains(result),
                "Actual: " + result
        );
    }

    @Test
    public void equalsTest() {
        Instant i1 = Instant.now();
        Instant i2 = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant i3 = Instant.now().plus(1, ChronoUnit.MINUTES);
        Instant i4 = Instant.now().plus(2, ChronoUnit.MINUTES);
        Timer timer1 = new Timer(i1, i2);
        Timer timer2 = new Timer(i1, i3);
        Timer timer3 = new Timer(i1, i4);

        assertEquals(timer1, timer2);
        assertNotEquals(timer1, timer3);
        assertFalse(timer1.equals(null));
    }

    @Test
    public void toStringTest() {
        Timer timer = new Timer(3, 0);
        assertTrue(timer.toString().matches(
                "Timer@[0-9a-f]*\\[(2:59|3:00|3:01)]"),
                "expect to match \"Timer@xxxxxxxx[2:59 or 3:00 or 3:01]\""
        );
    }
}
