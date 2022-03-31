package client.scenes;

import client.utils.LeaderboardHelper;
import com.google.inject.Inject;
import commons.GameState;
import commons.LeaderboardEntry;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class GameEndingCtrl {
    private MainCtrl mainCtrl;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;

    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardUsernames;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardRanks;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardScores;

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
        leaderboard.setItems(FXCollections.observableList(
            leaderboardHelper.prepareLeaderboard(gameState.leaderboard, mainCtrl.getCurrentUsername()))
        );
    }

    public void splashScreen(ActionEvent actionEvent) {
        mainCtrl.exitGame();
        mainCtrl.showSplashScreen();
    }
}
