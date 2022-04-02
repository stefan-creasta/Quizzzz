package client.utils;


import commons.LeaderboardEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SinglePlayerUtils {
    Scanner data;
    public List<LeaderboardEntry> entries;
    public final String filename = "leaderboard.txt";

    /**
     * Writes a leaderboard entry to a local text file, the location of which is dubiously placed right inside the client.
     * It should be used to save the leaderboard entry of a single-play-run at the end.
     * Would save a new entry every time it is called and does not overwrite the old data.
     * @param entry
     */
    public void writeLeaderboardEntry(LeaderboardEntry entry) {
            try{
                FileWriter fw = new FileWriter(filename,true);
                fw.write(entry.toString()+"\n");
                fw.close();
            }
                catch(IOException e){
                    e.printStackTrace();
            }
    }

    /**
     * Reads the text file in the client folder containing all the leaderboard entries.
     * It should be used to retrieve all the past results to load the data in the in-game leaderboard for the current
     * solo player to observe and compare themselves to.
     * @return
     * A list containing all the local leaderboard entry stored inside in the text file
     */
    public List<LeaderboardEntry> readLeaderboardInGame() {
        try {
            data = new Scanner(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        entries = new ArrayList<>();
        while (data.hasNextLine()) {
            String objectName, dateString, username,stuff,scoring;
            String[] date;
            String[] scoreLine;
            Integer score;
            objectName = data.nextLine();
            dateString = data.nextLine().split("=")[1];
            score = Integer.parseInt(data.nextLine().split("=")[1]);
            username = data.nextLine().split("=")[1];
            stuff = data.nextLine();


            LeaderboardEntry newEntry = new LeaderboardEntry(username, score);
            entries.add(newEntry);
        }
        data.close();
        return entries;
    }

}
