package commons;

import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

public class PlayerAnswer {

    public String answer;
    public long gameId;
    public long playerId;

    public PlayerAnswer(String answer, long gameId, long playerId) {
        this.answer = answer;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
