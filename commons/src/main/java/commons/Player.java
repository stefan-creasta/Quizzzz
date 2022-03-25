package commons;

public class Player {
    private static long idGenerator = 0L;

    public boolean doublePointsPower;//whether they have used a doublePointsPowerUp
    public boolean eliminateAnswerPower;//whether they have used an eliminate wrong answer power up
    public boolean reduceTimePower;//whether they have used a reduce time power up

    public boolean shouldReceiveDouble; // whether the player should recieve double points this round

    public long timeToAnswer;//time it took them to answer in milliseconds
    public long timeOfReceival;//time of receiving the question in milliseconds

    public long id;
    public String username;
    public long score;
    public long gameId;
//    public Timer timer;
    public String answer;


    public Player() {
        this.id = idGenerator++;
        this.username = null;
        this.score = 0;
//        this.timer = new Timer(0, 30);
        this.answer = null;
        this.gameId = -1;
        doublePointsPower = true;
        eliminateAnswerPower = true;
        reduceTimePower = true;
        timeToAnswer = -1;
        timeOfReceival = -1;
    }

    public Player(String username, long score) {
        this.id = idGenerator++;
        this.username = username;
        this.score = score;
//        this.timer = new Timer(0, 30);
        this.answer = null;
        this.gameId = -1;
        doublePointsPower = true;
        eliminateAnswerPower = true;
        reduceTimePower = true;
        timeToAnswer = -1;
        timeOfReceival = -1;
    }

}
