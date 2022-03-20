package commons;

public class Player {
    private static long idGenerator = 0L;

    public long id;
    public String username;
    public long score;
    public long gameId;
//    public Timer timer;
    public String answer;
    public long timeToAnswer;

    public Player(String username, long score) {
        this.id = idGenerator++;
        this.username = username;
        this.score = score;
//        this.timer = new Timer(0, 30);
        this.answer = null;
        this.gameId = -1;
        this.timeToAnswer = -1;
    }

}