package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

//@Entity
public class Game {
    public static long idGenerator = 0L;
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public int nr;
    public String quiz;
    public List<Player> players;
    public boolean started;

    private Game(){}

    public Game(String q){
        this.quiz = q;
        nr = 0;
        id = idGenerator++;
        players = new LinkedList<>();
        started = false;
    }

    public long getCurrentQuestion(){
        return (long) this.quiz.charAt(this.nr);
    }

    public boolean progressGame(){
        return ++this.nr < quiz.length();
    }

    public boolean addPlayer(Player player) {
        if (players.stream().anyMatch(x -> x.username.equals(player.username))) return false;
        players.add(player);
        player.gameId = id;
        return true;
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
