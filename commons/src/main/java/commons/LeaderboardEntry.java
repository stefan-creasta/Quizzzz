package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**class to hold entries of a leaderboard
 */
@Entity
public class LeaderboardEntry implements Comparable<LeaderboardEntry> {
    @Id
    public String username;

    public int score;
    public Date date;

    @Transient
    public int rank;

    private LeaderboardEntry() {
        this(null, 0);
    }

    public LeaderboardEntry(String un, int s) {
            this(un, s, new Date());
    }

    public LeaderboardEntry(String un, int s, Date d) {
        username = un;
        score = s;
        date = d;
        rank = -1;
    }

    /**compares the entry to another. When a list of entries is sorted in ascending order the first items will be the
     * ones with the highest score, the oldest one being the first.
     * @param other
     * @return
     */
    @Override
    public int compareTo(LeaderboardEntry other) {
        if (other.score == this.score) {
            return this.date.compareTo(other.date);
        }
        else {
            return other.score - this.score;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
