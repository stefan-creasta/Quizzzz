package client.utils;


import commons.LeaderboardEntry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SinglePlayerUtils {
    public Scanner data;
    public List<LeaderboardEntry> entries;
    public final String filename = "leaderboard.txt";
    private static DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

    public SinglePlayerUtils() {
        entries = new ArrayList<>();
    }
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
                entries.add(entry);
                Collections.sort(entries);
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
            File f = new File(filename);
            f.createNewFile();
            data = new Scanner(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<LeaderboardEntry>() ;
        }
        entries.removeIf(x -> true);
        while (data.hasNextLine()) {
            String objectName, dateString, username,stuff,scoring,ranks;
            Date date = null;
            String[] scoreLine;
            Double score;
            objectName = data.nextLine();
            dateString = data.nextLine();
            ranks = data.nextLine();
            boolean healthy = true;

            try {
            score = Double.parseDouble(data.nextLine().split("=")[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                score = 0.0;
                healthy = false;
            }

            username = data.nextLine().split("=")[1];
            try {
                date = dateFormat.parse(dateString.split("=")[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            stuff = data.nextLine();

            LeaderboardEntry newEntry;
            if (date == null)
                newEntry = new LeaderboardEntry(username, score);
            else
                newEntry = new LeaderboardEntry(username, score, date);
            if (healthy)
            entries.add(newEntry);
        }
        data.close();
        Collections.sort(entries);
        return entries;
    }

}
