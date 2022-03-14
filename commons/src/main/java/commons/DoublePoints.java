package commons;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("DOUBLE_POINTS")
public class DoublePoints extends PowerUp{
    public DoublePoints(){}
    public DoublePoints(String username, String time){
        super(username,time);
    }
}
