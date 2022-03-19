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
    public long timerSyncLong;
    public long duration;
    public long gameId;
    public long playerId;
    public String username;
    public String playerAnswer;
    public List<LeaderboardEntry> leaderboard;

    public GameState(long gameId, Question question, Player player, List<Player> players) {
        this.gameId = gameId;
        this.question = question;
        if (player != null) setPlayer(player);
        leaderboard = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            leaderboard.add(new LeaderboardEntry(players.get(i).username, 0));
        }
        Collections.sort(leaderboard);
        this.stage = Stage.LOBBY;

    }

    public void setPlayer(Player player) {
//        this.timerSyncLong = player.timer.getSynchronizationLong();
//        this.duration = player.timer.getDurationLong();
        this.playerId = player.id;
        this.username = player.username;
        this.playerAnswer = player.answer;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "stage=" + stage +
                ", question=" + question +
                ", isError=" + isError +
                ", message='" + message + '\'' +
                ", timerSyncLong=" + timerSyncLong +
                ", duration=" + duration +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", username='" + username + '\'' +
                ", playerAnswer='" + playerAnswer + '\'' +
                '}';
    }
}
