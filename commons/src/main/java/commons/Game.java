package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public class Game {
    public static long idGenerator = 0L;
    public long id;
    public List<Question> questions;
    public int currentQuestion;
    public List<Player> players;
    public boolean started;
    public GameState.Stage stage;
    private Game(){}

    public Game(List<Question> questions){
        this.id = idGenerator++;
        this.players = new LinkedList<>();
        this.questions = questions;
        this.currentQuestion = 0;
        this.started = false;
        this.stage = GameState.Stage.LOBBY;
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestion);
    }

    public boolean progressGame(){
        return ++this.currentQuestion < questions.size();

        //TODO: assign scores to each player
    }

    public boolean addPlayer(Player player) {
        if (players.stream().anyMatch(x -> x.username.equals(player.username))) return false;
        players.add(player);
        player.gameId = id;
        return true;
    }

    public GameState getState(Player player) {
        return new GameState(this, player);
    }

    public GameState getState() {
        return new GameState(this, null);
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
