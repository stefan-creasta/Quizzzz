package commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    public int currentQuestion;

    public List<Emote> emotes;

    public double timeToAnswer;//time it took them to answer in milliseconds
    public double timeOfReceival;//time of receiving the question in milliseconds
    public double thisScored;

    public GameState() {

    }
    /**
     * Create a GameState for a game tailored for a specific player. If player is null the constructor will skip setting the fields relevant to the player.
     * @param game the game
     * @param player the specific player that the GameState is going to be sent to
     */
    public GameState(Game game, Player player) {
        this.stage = game.stage;
        this.question = game.getCurrentQuestion();
        this.isError = false;
        this.message = null;
        this.gameId = game.id;
        timeOfReceival = -1;
        timeToAnswer = -1;
        instruction = "noInstruction";
        this.currentQuestion = game.currentQuestion;

        if (player != null) setPlayer(player);

        this.leaderboard = new ArrayList<>();

        this.emotes = game.emotes;

        for (Player lobbyPlayer : game.players) {
            this.leaderboard.add(new LeaderboardEntry(lobbyPlayer.username, lobbyPlayer.score));
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
                ", timerSyncLong=" + timerSyncLong +
                ", duration=" + duration +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", username='" + username + '\'' +
                ", playerAnswer='" + playerAnswer + '\'' +
                ", leaderboard=" + leaderboard +
                ", emotes=" + emotes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return isError == gameState.isError && timerSyncLong == gameState.timerSyncLong && duration == gameState.duration && gameId == gameState.gameId && playerId == gameState.playerId && currentQuestion == gameState.currentQuestion && Double.compare(gameState.timeToAnswer, timeToAnswer) == 0 && Double.compare(gameState.timeOfReceival, timeOfReceival) == 0 && Double.compare(gameState.thisScored, thisScored) == 0 && stage == gameState.stage && Objects.equals(question, gameState.question) && Objects.equals(message, gameState.message) && Objects.equals(instruction, gameState.instruction) && Objects.equals(username, gameState.username) && Objects.equals(playerAnswer, gameState.playerAnswer) && Objects.equals(leaderboard, gameState.leaderboard) && Objects.equals(emotes, gameState.emotes);
    }

}
