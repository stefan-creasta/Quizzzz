package commons;

import javax.persistence.DiscriminatorValue;

@DiscriminatorValue("ELIMINATE_WRONG_ANSWER")
public class EliminateWrongAnswer extends PowerUp{
    public EliminateWrongAnswer(){}
    public EliminateWrongAnswer(String username, String time){
            super(username,time);
    }
}
