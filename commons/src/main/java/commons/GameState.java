package commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    public enum Stage {LOBBY, QUESTION, INTERVAL};
    //roughly stores which screen should be displayed during the game session.
    public Stage stage;
    public Question question;
    //set to true if only the playerId needs to be processed.
    public boolean isError;
    public String message;
    public String instruction;
    public long timerSyncLong;
    public long duration;
    public long gameId;
    public long playerId;
    public String username;
    public String playerAnswer;
    public List<LeaderboardEntry> leaderboard;
    public boolean halfTime;

    public List<Emote> emotes;

    public long timeToAnswer;//time it took them to answer in milliseconds
    public long timeOfReceival;//time of receiving the question in milliseconds

    public GameState() {

    }
    /**
     * Create a GameState for a game tailored for a specific player. If player is null the constructor will skip setting the fields relevant to the player.
     * @param game the game
     * @param player the specific player that the GameState is going to be sent to
     */
    public GameState(Game game, Player player) {
        this.halfTime = false;
        this.stage = game.stage;
        this.question = game.getCurrentQuestion();
        this.isError = false;
        this.message = null;
        this.gameId = game.id;
        timeOfReceival = -1;
        timeToAnswer = -1;
        instruction = "noInstruction";

        if (player != null) setPlayer(player);

        this.leaderboard = new ArrayList<>();

        this.emotes = game.emotes;

        for (Player lobbyPlayer : game.players) {
            this.leaderboard.add(new LeaderboardEntry(lobbyPlayer.username, (int) lobbyPlayer.score));
        }
        Collections.sort(this.leaderboard);
    }

    /**Sets the relevant fields for the specific player that the GameState is going to be sent to.
     * @param player the specific player that the GameState is going to be sent to
     */
    public void setPlayer(Player player) {
//        this.timerSyncLong = player.timer.getSynchronizationLong();
//        this.duration = player.timer.getDurationLong();
        this.playerId = player.id;
        this.username = player.username;
        this.playerAnswer = player.answer;
    }

    public void setPlayerAnswer(String answer) {
        this.playerAnswer = answer;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "stage=" + stage +
                ", question=" + question +
                ", isError=" + isError +
                ", message='" + message + '\'' +
                ", instruction='" + instruction + '\'' +
                ", timerSyncLong=" + timerSyncLong +
                ", duration=" + duration +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", username='" + username + '\'' +
                ", playerAnswer='" + playerAnswer + '\'' +
                ", leaderboard=" + leaderboard +
                ", halfTime=" + halfTime +
                ", emotes=" + emotes +
                ", timeToAnswer=" + timeToAnswer +
                ", timeOfReceival=" + timeOfReceival +
                '}';
    }
}
