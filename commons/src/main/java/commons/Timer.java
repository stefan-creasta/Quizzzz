package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Timer {
    private Instant startTime;
    private Instant endTime;

    /**Create a timer. Intended to be used on the client side to be synced.
     * On the next server message, call setDuration(long) and synchronize(long) in order.
     */
    public Timer() {
        startTime = Instant.now();
        endTime = Instant.now();
    }

    /**Create a timer. Intended to be used on the server side.
     * @param minutes
     * @param seconds
     */
    public Timer(int minutes, int seconds) {
        startTime = Instant.now();
        setDuration(minutes, seconds);
    }

    /**Synchronize this Timer with another
     * @param elapsed the output from other.getSynchronizationLong()
     * @return the time from old to new startTime
     */
    public Duration synchronize(long elapsed) {
        Instant oldStartTime = startTime;
        Duration duration = getDuration();
        startTime = Instant.now().minus(elapsed, ChronoUnit.MICROS);
        endTime = startTime.plus(duration);
        return Duration.between(startTime, oldStartTime);
    }

    /**See: synchronize(long)
     * @return the synchronization long
     */
    public long getSynchronizationLong() {
        Duration duration = Duration.between(startTime, Instant.now());
        return duration.dividedBy(ChronoUnit.MICROS.getDuration());
    }

    /**Get duration.  Duration has useful methods like dividedBy(long)
     * @return
     */
    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    /**See: setDuration(long)
     * @return the duration long
     */
    public long getDurationLong() {
        return getDuration().dividedBy(ChronoUnit.MICROS.getDuration());
    }

    /**Set duration to the given Duration. Duration has useful methods like dividedBy(long)
     * @param duration
     */
    public void setDuration(Duration duration) {
        endTime = startTime.plus(duration);
    }

    /**Set the duration by minutes and seconds
     * @param minutes
     * @param seconds
     */
    public void setDuration(int minutes, int seconds) {
        endTime = startTime.plus(60 * minutes + seconds, ChronoUnit.SECONDS);
    }

    /**Synchronize the duration to another Timer
     * @param duration the output of other.getSynchronizationLong()
     */
    public void setDuration(long duration) {
        endTime = startTime.plus(duration, ChronoUnit.MICROS);
    }

    /**Check if the timer was elapsed
     * @return
     */
    public boolean isElapsed() {
        return Instant.now().compareTo(endTime) <= 0;
    }

    /**Get the remaining time in m:ss format
     * @return
     */
    public String toTimerDisplayString() {
        long left = Duration.between(Instant.now(), endTime).getSeconds();
        return String.format("%d:%02d", left / 60, Math.abs(left % 60));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return String.format("Timer@%s[%s]", Integer.toUnsignedString(hashCode(), 16), toTimerDisplayString());
    }
}
