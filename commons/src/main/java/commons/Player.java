package commons;

public class Player {
    private static long idGenerator = 0L;

    public boolean doublePointsPower;//whether they have used a doublePointsPowerUp
    public boolean eliminateAnswerPower;//whether they have used an eliminate wrong answer power up
    public boolean reduceTimePower;//whether they have used a reduce time power up

    public boolean shouldReceiveDouble; // whether the player should recieve double points this round

    public long id;
    public String username;
    public long score;
    public long gameId;
//    public Timer timer;
    public String answer;
    public long timeToAnswer;

    public Player() {
        this.id = idGenerator++;
        this.username = null;
        this.score = 0;
//        this.timer = new Timer(0, 30);
        this.answer = null;
        this.gameId = -1;
        this.timeToAnswer = -1;
    }

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
