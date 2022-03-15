package commons;

public class GameState {
    public enum Stage {QUESTION, INTERVAL};
    //roughly stores which screen should be displayed during the game session.
    public Stage stage;
    public Question question;
    //set to true if only the playerId needs to be processed.
    public boolean isError;
    public String message;
    public long timerSyncLong;
    public long duration;
    public long playerId;
    public String username;
    public String playerAnswer;

    public GameState(Question question, Player player) {
        this.question = question;
        if (player != null) setPlayer(player);
    }

    public void setPlayer(Player player) {
        this.timerSyncLong = player.timer.getSynchronizationLong();
        this.duration = player.timer.getDurationLong();
        this.playerId = player.id;
        this.username = player.username;
        this.playerAnswer = player.answer;
    }
}
