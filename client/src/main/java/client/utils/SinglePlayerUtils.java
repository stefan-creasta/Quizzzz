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
    public List<LeaderboardEntry> readLeaderboardInGame() {
        try {
            data = new Scanner(new File(filename)).useDelimiter(",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        entries = new ArrayList<>();
        while (data.hasNextLine()) {
            Integer score;
            String username, dateString;
            dateString = data.next();
            score = Integer.parseInt(data.next());
            username = data.next();
            LeaderboardEntry newEntry = new LeaderboardEntry(username, score);
            entries.add(newEntry);
        }
        data.close();
        return entries;
    }

}
