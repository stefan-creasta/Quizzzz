package commons;

import java.time.Duration;

public class GameState {
    public enum Stage {QUESTION, INTERVAL};

    //roughly stores which screen should be displayed during the game session.
    public Stage stage;
    public Question question;
    private Player player;
    //set to true if only the playerId needs to be processed.
    public boolean isError;
    public String message;
    public long timerSyncLong;
    public Duration duration;

    public GameState(Question question, Player player) {
        this.player = player;
        this.question = question;
        if (player != null) setPlayer(player);
    }

    public void setPlayer(Player player) {
        this.timerSyncLong = player.timer.getSynchronizationLong();
        this.duration = player.timer.getDuration();
    }

    public Player getPlayer() {
        return player;
    }
}
