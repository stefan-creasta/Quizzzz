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
    }

    public void handleGameState(GameState gameState) {
        if (mainCtrl.singleplayerGame) {
            newGameButton.setText("Play as Singleplayer Again");
        }
        else {
            newGameButton.setText("Play as Multiplayer Again");
        }
        leaderboard.setItems(FXCollections.observableList(
            leaderboardHelper.prepareLeaderboard(gameState.leaderboard, mainCtrl.getCurrentUsername()))
        );
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
            mainCtrl.setupMultiplayerGame();
            mainCtrl.joinGame(mainCtrl.getCurrentUsername());
        }
    }
    public void showServerLeaderboard(){
        updateLeaderboard();

    }
    public void updateLeaderboard() {
        try {
            entries = mainCtrl.getServerLeaderboards();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
