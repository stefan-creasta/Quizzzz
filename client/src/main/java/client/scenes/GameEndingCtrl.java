package client.scenes;

import client.utils.LeaderboardHelper;
import com.google.inject.Inject;
import commons.GameState;
import commons.LeaderboardEntry;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class GameEndingCtrl {
    private MainCtrl mainCtrl;

    private List<LeaderboardEntry> entries;

    @FXML
    private Label currentleaderboard;

    @FXML
    private Label worldleaderboard;

    @FXML
    private TableView<LeaderboardEntry> serverleaderboard;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;

    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardUsernames;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardRanks;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardScores;

    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardUsernames1;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardRanks1;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardScores1;

    @FXML
    private Button newGameButton;

    @FXML
    private Button backButton;

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
        leaderboardHelper.setRankColumnCellFactory(leaderboardRanks1);
        leaderboardHelper.setUsernameColumnCellFactory(leaderboardUsernames1);
        leaderboardHelper.setScoreColumnCellFactory(leaderboardScores1);
        currentleaderboard.setVisible(true);
        serverleaderboard.setVisible(false);
        worldleaderboard.setVisible(false);
        backButton.setVisible(false);
    }

    public void handleGameState(GameState gameState) {
        if (mainCtrl.singleplayerGame) {
            newGameButton.setText("Play as Singleplayer Again");
        }
        else {
            newGameButton.setText("Play as Multiplayer Again");
        }
        updateServerLeaderboard(gameState);
        leaderboard.setItems(FXCollections.observableList(
            leaderboardHelper.prepareLeaderboard(gameState.leaderboard, mainCtrl.getCurrentUsername()))
        );
        updateLeaderboard();
        System.out.println(entries);

        serverleaderboard.setItems(FXCollections.observableList(
                leaderboardHelper.prepareLeaderboard(entries, mainCtrl.getCurrentUsername())));
    }

    public void splashScreen(ActionEvent actionEvent) {
        mainCtrl.exitGame();
        mainCtrl.showSplashScreen();
    }

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

    public void showServerLeaderboard(){
        serverleaderboard.setVisible(true);
        leaderboard.setVisible(false); //current leaderboard
        currentleaderboard.setVisible(false); //these are labels 4head
        worldleaderboard.setVisible(true); //these are labels 4head
        backButton.setVisible(true);

    }
    //after adding the entries to the server, we receive the top 10 sorted entries
    public void updateLeaderboard() {
        try {
            entries = mainCtrl.getServerLeaderboards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void goBack(){
        serverleaderboard.setVisible(false);
        worldleaderboard.setVisible(false);
        leaderboard.setVisible(true);
        currentleaderboard.setVisible(true);
        backButton.setVisible(false);

    }
    // recieves the leaderboard from the gamestate and add it to the database on the server
    public void updateServerLeaderboard(GameState state){
        try {
            for (LeaderboardEntry e:state.leaderboard) {
                if (state.username.equals(e.username))
                mainCtrl.updateServerLeaderboard(e);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
