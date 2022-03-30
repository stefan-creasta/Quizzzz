package client.scenes;

import client.Communication.ServerListener;
import com.google.inject.Inject;
import commons.GameState;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.util.LinkedList;
import java.util.List;

import static javafx.beans.binding.Bindings.createObjectBinding;

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

    private final List<LeaderboardEntry> completeLeaderboard = new LinkedList<>();

    @Inject
    public GameEndingCtrl(MainCtrl mainCtrl, ServerListener listener) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initialize() {
        leaderboardRanks.setCellFactory(e -> {
            TableCell<LeaderboardEntry, String> indexCell = new TableCell<>();
            var rowProperty = indexCell.tableRowProperty();
            var rowBinding = createObjectBinding(() -> {
                TableRow<LeaderboardEntry> row = rowProperty.get();
                if (row != null) {
                    //This is why completeLeaderboard needs to be final.
                    int rowIndex = completeLeaderboard.indexOf(row.getItem());
                    if (row.getItem() == null) {
                        return "";
                    }
                    else if (rowIndex < row.getTableView().getItems().size()) {
                        return "#" + Integer.toString(rowIndex + 1);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });
        leaderboardUsernames.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "..." : e.getValue().username));
        leaderboardScores.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "" : Integer.toString(e.getValue().score)));
    }

    public void handleGameState(GameState gameState) {
        leaderboard.setItems(FXCollections.observableList(prepareLeaderboard(gameState.leaderboard, mainCtrl.getCurrentUsername())));
    }

    /**
     * Creates the truncated list of the LeaderboardEntries which will be displayed on the screen.
     * @param entries all entries in the leaderboard
     * @param currentUsername the username that must be displayed i.e. the local player username
     * @return
     */
    public List<LeaderboardEntry> prepareLeaderboard(List<LeaderboardEntry> entries, String currentUsername) {
        //This variable cannot be set because it needs to be final.
        completeLeaderboard.removeIf(x -> true);
        entries.forEach(completeLeaderboard::add);
        List<LeaderboardEntry> displayedLeaderboard = new LinkedList<>(
                entries.subList(0, Math.min(entries.size(), 5))
        );
        LeaderboardEntry localPlayerEntry = entries.stream().filter(
                x -> x.username.equals(currentUsername)
        ).findFirst().get();

        if (!displayedLeaderboard.contains(localPlayerEntry)) {
            if (entries.indexOf(localPlayerEntry) != 5)
                //null is displayed as "..."
                displayedLeaderboard.add(null);
            displayedLeaderboard.add(localPlayerEntry);
        }
        return displayedLeaderboard;
    }

    public void splashScreen(ActionEvent actionEvent) {
        mainCtrl.exitGame();
        mainCtrl.showSplashScreen();
    }
}
