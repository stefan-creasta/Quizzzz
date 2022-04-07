package client.utils;

import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

import java.util.LinkedList;
import java.util.List;

public class LeaderboardHelper {
    private int topN = -1;

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
        column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "" : ("#" + e.getValue().rank)));
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
        column.setCellValueFactory(e -> new SimpleStringProperty(e.getValue() == null ? "" : String.format("%.2f", e.getValue().score)));
    }

    /**
     * Creates the truncated list of the LeaderboardEntries which will be displayed on the screen.
     * @param entries all entries in the leaderboard
     * @param currentUsername the username that must be displayed i.e. the local player username
     * @return
     */
    public List<LeaderboardEntry> prepareLeaderboard(List<LeaderboardEntry> entries, String currentUsername) {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).rank = i + 1;
        }

        if (topN == -1) {
            return new LinkedList<>(entries);
        }
        else {
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
    public List<LeaderboardEntry> prepareLeaderboard(List<LeaderboardEntry> entries) {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).rank = i + 1;
        }

        if (topN == -1) {
            return new LinkedList<>(entries);
        }
        else {
            List<LeaderboardEntry> displayedLeaderboard = new LinkedList<>(
                    entries.subList(0, Math.min(entries.size(), topN))
            );
            LeaderboardEntry localPlayerEntry = entries.stream().findFirst().get();

            if (!displayedLeaderboard.contains(localPlayerEntry)) {
                if (entries.indexOf(localPlayerEntry) != topN)
                    //null is displayed as "..."
                    displayedLeaderboard.add(null);
                displayedLeaderboard.add(localPlayerEntry);
            }
            return displayedLeaderboard;
        }
    }

}
