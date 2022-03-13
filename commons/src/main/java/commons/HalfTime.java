package commons;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("HALF_TIME")
public class HalfTime extends PowerUp {
    public HalfTime(){}
    public HalfTime(String username, String time) {
        super(username, time);

    }
}
