package client.scenes;

import client.utils.LeaderboardHelper;
import com.google.inject.Inject;
import commons.GameState;
import commons.LeaderboardEntry;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class GameEndingCtrl {
    private MainCtrl mainCtrl;

    private List<LeaderboardEntry> thisgameentries;

    private List<LeaderboardEntry> entries;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;

    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardUsernames;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardRanks;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardScores;

    @FXML
    private Button newGameButton;

    @FXML
    private Button backButton;

    @FXML
    private Button worldLeaderboardButton;

    @FXML
    private AnchorPane root;

    private LeaderboardHelper leaderboardHelper;

    @Inject
    public GameEndingCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.leaderboardHelper = new LeaderboardHelper();
    }

    @FXML
    public void initialize() {
        leaderboardHelper.setRankColumnCellFactory(leaderboardRanks);
        leaderboardHelper.setUsernameColumnCellFactory(leaderboardUsernames);
        leaderboardHelper.setScoreColumnCellFactory(leaderboardScores);
        backButton.setVisible(false);
    }

    public void handleGameState(GameState gameState) {
        thisgameentries = gameState.leaderboard;
        if (mainCtrl.singleplayerGame) {
            worldLeaderboardButton.setText("Past Singleplayer Scores");
            newGameButton.setText("Play as Singleplayer Again");
        }
        else {
            worldLeaderboardButton.setText("World Leaderboard");
            newGameButton.setText("Play as Multiplayer Again");
        }
        updateServerLeaderboard(gameState);
        leaderboard.setItems(FXCollections.observableList(
            leaderboardHelper.prepareLeaderboard(thisgameentries, mainCtrl.getCurrentUsername()))
        );
        System.out.println(entries);

    }

    public void splashScreen(ActionEvent actionEvent) {
        mainCtrl.exitGame();
        mainCtrl.showSplashScreen();
    }

    /**
     * A user could start a new singleplayer game with the current username
     * Or join a new lobby with the current name
     * @param actionEvent
     */

    public void newGame(ActionEvent actionEvent) {
        mainCtrl.exitGame();
        if (mainCtrl.singleplayerGame) {
            mainCtrl.setupSingleplayerGame();
            mainCtrl.initiateSingleplayerGame(new commons.Player(mainCtrl.getCurrentUsername(), 0));
        }
        else {
            if(mainCtrl.setupMultiplayerGame())
            mainCtrl.joinGame(mainCtrl.getCurrentUsername());
        }
    }

    public void showOtherLeaderboard() {
        if (mainCtrl.singleplayerGame) {
            showSingleplayerLeaderboard();
        } else {
            showServerLeaderboard();
        }
    }

    /**
     * Displays the leaderboard containing the top 10 players on that specific server
     */
    public void showServerLeaderboard(){
        worldLeaderboardButton.setVisible(false);
        updateLeaderboard();
        leaderboard.setItems(FXCollections.observableList(
                leaderboardHelper.prepareLeaderboard(entries, mainCtrl.getCurrentUsername()))
        );
        leaderboard.setVisible(true); //current leaderboard
        backButton.setVisible(true);

    }

    public void showSingleplayerLeaderboard() {
        worldLeaderboardButton.setVisible(false);
        try {
            entries = mainCtrl.getSingleplayerLeaderboards();
            leaderboard.setItems(FXCollections.observableList(
                    leaderboardHelper.prepareLeaderboard(entries, mainCtrl.getCurrentUsername()))
            );
            leaderboard.setVisible(true); //current leaderboard
            backButton.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method retrieves the list of the 10 leaderboard entries sorted by score from the server.
     * It is called after everyone's entries in the current game has been sent to the server to be sorted.
     */
    public void updateLeaderboard() {
        try {
            entries = mainCtrl.getServerLeaderboards();
            System.out.println(entries);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to return to the game ending screen after accessing the server leaderboard
     */
    public void goBack(){
        worldLeaderboardButton.setVisible(true);
        leaderboard.setItems(FXCollections.observableList(
                leaderboardHelper.prepareLeaderboard(thisgameentries, mainCtrl.getCurrentUsername()))
        );
        leaderboard.setVisible(true);
        backButton.setVisible(false);

    }

    /**
     * After the game ends, this method sends the entry for the current player the database to be sorted
     * @param state
     */
    public void updateServerLeaderboard(GameState state){
        try {
            for (LeaderboardEntry e:state.leaderboard) {
                if (state.username.equals(e.username))
                mainCtrl.updateServerLeaderboard(e);
                System.out.println(e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
