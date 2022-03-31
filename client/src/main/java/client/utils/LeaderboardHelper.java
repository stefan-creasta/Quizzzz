package client.utils;

import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;

import java.util.LinkedList;
import java.util.List;

import static javafx.beans.binding.Bindings.createObjectBinding;

public class LeaderboardHelper {
    private final List<LeaderboardEntry> completeLeaderboard = new LinkedList<>();
    private int topN = 5;

    public LeaderboardHelper() {}

    /**Sets the maximum number of top entries that are always displayed.
     * @param topN the maximum number
     */
    public void setTopN(int topN) {
        this.topN = topN;
    }

    /**
     * Sets the cell factory for the rank column.
     * @param column the rank TableColumn
     */
    public void setRankColumnCellFactory(TableColumn<LeaderboardEntry, String> column) {
        column.setCellFactory(e -> {
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
    }

    /**
     * Sets the cell value factory for the username column.
     * @param column the username TableColumn
     */
    public void setUsernameColumnCellFactory(TableColumn<LeaderboardEntry, String> column) {
        column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "..." : e.getValue().username));
    }

    /**
     * Sets the cell value factory for the score column.
     * @param column the score TableColumn
     */
    public void setScoreColumnCellFactory(TableColumn<LeaderboardEntry, String> column) {
        column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "" : Integer.toString(e.getValue().score)));
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
                entries.subList(0, Math.min(entries.size(), topN))
        );
        LeaderboardEntry localPlayerEntry = entries.stream().filter(
                x -> x.username.equals(currentUsername)
        ).findFirst().get();

        if (!displayedLeaderboard.contains(localPlayerEntry)) {
            if (entries.indexOf(localPlayerEntry) != topN)
                //null is displayed as "..."
                displayedLeaderboard.add(null);
            displayedLeaderboard.add(localPlayerEntry);
        }
        return displayedLeaderboard;
    }
}
