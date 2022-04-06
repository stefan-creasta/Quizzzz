package server.service;

import commons.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static commons.GameState.Stage.QUESTION;

@Service
public class GameService {

    static class GameRepository {
        static long idGenerator = 0;
        private Map<Long, Game> games;

        GameRepository() {
            games = new HashMap<Long, Game>();
        }

        /**
         * Gets the game with the according ID
         *
         * @param id the ID of the game
         * @return the Game instance
         * @throws IllegalArgumentException
         */
        Game getId(long id) throws IllegalArgumentException {
            if (!existsById(id)) throw new IllegalArgumentException();
            return games.get(id);
        }

        void save(Game game) {
            Objects.requireNonNull(game, "game cannot be null");
            game.id = idGenerator++;
            games.put(game.id, game);
        }

        /**
         * Checks whether the game with a certain id exists
         * @param id the game id
         * @return true, if the game with a certain id exists, false, otherwise
         */
        boolean existsById(long id) {
            return games.containsKey(id);
        }
    }

    static class PlayerRepository {
        static long idGenerator;
        private Map<Long, Player> players;

        PlayerRepository() {
            players = new HashMap<>();
        }

        Player getId(long id) throws IllegalArgumentException {
            if (!existsById(id)) throw new IllegalArgumentException();
            return players.get(id);
        }

        void save(Player player) {
            Objects.requireNonNull(player, "player cannot be null");
            player.id = idGenerator++;
            players.put(player.id, player);
        }

        /**
         * Checks whether the player with a certain id exists
         * @param id the player id
         * @return true, if the player with a certain id exists, false, otherwise
         */
        boolean existsById(long id) {
            return players.containsKey(id);
        }

        /**
         * Gets the list of players in the repository
         * @return the list of players
         */
        Map<Long, Player> getPlayers() {
            return players;
        }
    }

    //currentGame is the game where new players will join. Basically the lobby
    private Game currentGame;

    private final GameRepository gameRepository = new GameRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final QuestionService questionService;
    private final LongPollingService longPollingService;
    private long timeOfSent;

    public String stateString = "";

    @Autowired
    public GameService(QuestionService questionService, LongPollingService longPollingService) {
        this.questionService = questionService;
        this.longPollingService = longPollingService;
    }

    /**
     * Gets called when a player presses on a power up button.
     * The points the player gets from this answer will be doubled
     *
     * @param playerID The ID of the player using the power up
     * @param gameID   the ID of the game the player is playing in
     */
    public String doublePointsPowerUp(long playerID, long gameID) {

        Game g = gameRepository.getId(gameID);
        Player pl = null;
        for (Player p : g.players) {
            if (p.id == playerID) {
                pl = p;
                break;
            }
        }
        try {
            if (pl.doublePointsPower) {//check if power up has been used already
                //TODO tell other users to showcase that someone used this power up
                pl.doublePointsPower = false;
                pl.shouldReceiveDouble = true;
                return "doublePointsPowerUp___success";
            }
            return "doublePointsPowerUp___fail";//is returned when the player has already used the power up
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * Gets called when a player tries to use an eliminate wrong answer power up. If the player has not used the power up before,
     * a random wrong answer gets sent to the client.
     *
     * @param playerID The playerID of the client using the power up.
     * @param gameID   The gameID of the game that the client is playing in.
     * @return a string saying whether the power up was successfully used and the wrong answer.
     */
    public String eliminateWrongAnswerPowerUp(long playerID, long gameID) {

        Game g = gameRepository.getId(gameID);
        Player pl = null;
        for (Player p : g.players) {
            if (p.id == playerID) {
                pl = p;
                break;
            }
        }
        try {
            if (pl.eliminateAnswerPower) {
                //TODO tell other users to showcase that someone used this power up
                pl.eliminateAnswerPower = false;
                Question q = g.questions.get(g.currentQuestion);
                int randomIndex = new Random().nextInt(2);
                String wrongAnswer;

                if (randomIndex == 0) {
                    wrongAnswer = q.wrongAnswer1;
                } else {
                    wrongAnswer = q.wrongAnswer2;
                }
                return "eliminateWrongAnswerPowerUp___success___" + wrongAnswer;
            }
            return "eliminateWrongAnswerPowerUp___fail";//gets returned when the user has already used the power up
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * Gets called when a player tries to use the half time power up. If the player has not used the power up before,
     * all other players receive a message that their time is halved and the according changes take place on the client
     * side. On the server side, the time of every player, during which they are allowed to submit an answer is
     * changed accordingly.
     *
     * @param playerID The playerID of the client using the power up.
     * @param gameID   The gameID of the game that the client is playing in.
     * @return a string saying whether the power up was successfully used and the wrong answer.
     */
    public String halfTimePowerUp(long playerID, long gameID) {

        Game g = gameRepository.getId(gameID);
        Player pl = null;
        for (Player p : g.players) {
            if (p.id == playerID) {
                pl = p;
                break;
            }
        }
        try{
            if(pl.reduceTimePower){
                pl.reduceTimePower = false;
                List<Player> players = g.players;
                for(Player p:players){
                    if(p.id!=playerID){//sends it to everyone except the player using the power up
                        GameState gState = g.getState();
                        gState.timeOfReceival = p.timeOfReceival;
                        double toReduce = 0.5;//alter percentages if you want to by changing the double
                        gState.timeOfReceival = gState.timeOfReceival - (10000 - (new Date().getTime() - gState.timeOfReceival))*toReduce;
                        p.timeOfReceival = gState.timeOfReceival;
                        gState.instruction = "halfTimePowerUp";
                        sendToPlayer(p.id, gState);

                    }
                }
                return "halfTimePowerUp___success___";
            }
            return "halfTimePowerUp___fail";//executes when the player has already used the power up
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }


    /**
     * A method which creates the current game - which is the lobby
     */
    private void createCurrentGame() {
        List<Question> questions = new ArrayList<>();
        questions = questionService.getRandom();
        currentGame = new Game(questions);
        gameRepository.save(currentGame);
    }

    /**
     * Gets a game from the gameRepository by its ID
     *
     * @param id the id of the game
     * @return the game
     */
    public Game getId(long id) {
        return gameRepository.getId(id);
    }

    /**
     * Produce a gameId which the player client can join a game or its lobby with
     * This method can be used even if there is only one lobby that can be joined at a given moment. Then it would
     * return the gameId of the game that the player is wanted to join.
     *
     * @return the gameId
     */
    public long createGame() {
        if (currentGame == null)
            createCurrentGame();
        return currentGame.id;
    }

    /**
     * Produce a gameId which the player client can join a singleplayer game
     * @return the gameId
     */
    public long createSingleplayerGame() {
        List<Question> questions = new ArrayList<>();
        questions = questionService.getRandom();
        Game singleplayerGame = new Game(questions);
        gameRepository.save(singleplayerGame);
        return singleplayerGame.id;
    }

    /**
     * Register *one player* into a game with the given username. This will fail if the lobby of the game already
     * has a player with the given username.
     *
     * @param gameId   the id of the game to join
     * @param username the username that the player wants to join with
     * @return the GameState to initially render the lobby or game screen
     * @throws IllegalArgumentException if the gameId is invalid.
     */
    public GameState joinGame(long gameId, String username) throws IllegalArgumentException {
        System.out.println("Username for player: " + username);
        Player player = new Player(username, 0);
        Game game = gameRepository.getId(gameId);
        GameState state = game.getState(player);
        if (!game.addPlayer(player)) {
            state.isError = true;
            state.message = "usernameAlreadyInGame";
            System.out.println("Username already taken");
            return state;
        }
        for (Player otherPlayer : game.players) {
            state = game.getState(otherPlayer);
            state.instruction = "joinGame";
            sendToPlayer(otherPlayer.id, state);
        }
        playerRepository.save(player);
        return state;
    }

    public GameState joinSingleplayerGame(long gameId, String username) throws IllegalArgumentException {
        System.out.println("Username for player: " + username);
        Player player = new Player(username, 0);
        Game game = gameRepository.getId(gameId);
        GameState state = game.getState(player);
        game.addPlayer(player);
        state = game.getState();
        state.setPlayer(player);
        state.instruction = "joinSinglePlayer";
        sendToPlayer(player.id, state);
        playerRepository.save(player);
        return state;
    }
    /**
     * Initiates a game. Do not allow other players to join and show the first question.
     * At the same time, it also creates a new current game (a new lobby)
     *
     * @param gameId the id of the game to initiate
     * @throws IllegalArgumentException if the gameId is invalid.
     */
    public void initiateGame(long gameId) throws IllegalArgumentException {
        Game game = gameRepository.getId(gameId);

        if (game.started) return;
        game.started = true;
        game.stage = QUESTION;
        pausePhase(game);
        createCurrentGame();
    }

    /**
     * Initiates a singleplayer game
     * @param gameId the id of the game to initiate
     * @throws IllegalArgumentException if the gameId is invalid.
     */
    public void initiateSingleplayerGame(long gameId) throws IllegalArgumentException {
        Game game = gameRepository.getId(gameId);

        if (game.started) return;
        game.started = true;
        game.stage = QUESTION;
        pausePhase(game);
    }

    /**
     * Method which returns the list of players for a certain game
     * @param id the game's id
     * @return the list of players
     */
    public List<String> getPlayers(long id) {
        Map<Long, Player> players = playerRepository.getPlayers();
        List<String> playersForGame = new ArrayList<>();
        for (Long playerId : players.keySet()) {
            Player player = players.get(playerId);
            if (player.gameId == id) {
                playersForGame.add(player.username);
            }
        }
        System.out.println("Players in the list: " + playersForGame.size());
        System.out.println("Player in the repository: " + players.entrySet().size());
        return playersForGame;
    }

    /**
     * Method which returns a leaderboard for a certain game
     * @param id the game's id
     * @return the list of leaderboard entries
     */
    public List<LeaderboardEntry> getLeaderboard(long id) {
        Map<Long, Player> players = playerRepository.getPlayers();
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        for(Long playerId : players.keySet()) {
            Player player = players.get(playerId);
            if(player.gameId == id) {
                LeaderboardEntry newLeaderboardEntry = new LeaderboardEntry(player.username, player.score);
                leaderboardEntries.add(newLeaderboardEntry);
            }
        }
        return leaderboardEntries;
    }

/**
     * Method which checks whether a username has already been taken
     * @param id the id of the game
     * @param username the new player's username
     * @return false if the username is already taken, true, otherwise
     */
    public boolean checkUsername(long id, String username) {

        List<String> players = getPlayers(id);
        if(players.contains(username) == true) {
            return false;
        }
        return true;
    }
    //methods that call each other back and forth to have a single game running.
    public void questionPhase(final Game game) {

        game.stage = GameState.Stage.QUESTION;

        stateString = "QUESTION";

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Player player : game.players) {
            GameState state = game.getState(player);
            state.stage = QUESTION;
            state.timeOfReceival = new Date().getTime();//current time in milliseconds since some arbitrary time in the past
            player.timeOfReceival = state.timeOfReceival;
            state.instruction = "questionPhase";
            sendToPlayer(player.id, state);
        }

        //switch to interval phase 30 seconds later so we make sure all player timers have run out.
        //TODO: have a flexible delay in case everyone's timer is shortened.
        new Thread(() -> {
            try {
                Thread.sleep(10_000);
                intervalPhase(game);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void intervalPhase(final Game game) {

        stateString = "INTERVAL";

        game.stage = GameState.Stage.INTERVAL;

        for (Player player : game.players) {
            GameState state = game.getState(player);
            state.setPlayerAnswer(player.answer);
            state.instruction = "intervalPhase";
            sendToPlayer(player.id, state);
        }
        score(game);
        //switch to question phase 5 seconds later so the player has time to see the correctness of their answer

            new Thread(() -> {
                try {
                    Thread.sleep(5_000);
                        if (game.currentQuestion + 1 < game.questions.size()) {
                        game.progressGame();
                        pausePhase(game);
                    } else {
                        endingPhase(game);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
    }

    public void pausePhase(final Game game) {
        stateString = "PAUSE";

        for (Player player : game.players) {
            GameState state = game.getState(player);
            state.setPlayerAnswer(player.answer);
            state.instruction = "pausePhase";
            sendToPlayer(player.id, state);
        }

        new Thread(() -> {
            try {
                Thread.sleep(8_00);
                questionPhase(game);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void endingPhase(final Game game) {
        for (Player player : game.players) {
            GameState state = game.getState(player);
            state.instruction = "endingPhase";
            sendToPlayer(player.id, state);
        }
    }

    public boolean existsById(long id) {
        return gameRepository.existsById(id);
    }

    /**
     * Send the game state to a player.
     * <p>
     * Note that the connection is only available after the player makes a request to
     * /api/listen, therefore you can't just send new data to a player without some
     * delay in between!
     * <p>
     * NOTE!!! to use the function, set the gameState's instruction field to some string instruction
     * and make a case in the switch statement for the instruction in handleGameState.
     *
     * @param playerId
     * @param state
     */
    public void sendToPlayer(Long playerId, GameState state) {
        longPollingService.sendToPlayer(playerId, state);
        timeOfSent = System.nanoTime();
    }

    /**
     * Gets called when a player clicks on the submit button for an answer. If they have not submitted an answer
     * already, their answer gets saved, as well as the time at which they answered so that the scoring later on
     * can be done according to the time of answering.
     *
     * @param playerId The id of the player who answered
     * @param ans      The contents of the answer button they clicked - their answer
     * @param gameId   The id of the game they are playing
     */
    public void submitByPlayer(Long playerId, String ans, Long gameId) {

        Game g = gameRepository.getId(gameId);
        Player p;
        int pos = 0;
        while (pos < g.players.size() && g.players.get(pos).id != playerId) {
            pos++;
        }
        p = g.players.get(pos);
        if((p.answer == null || p.answer.isEmpty())&&stateString.equals("QUESTION")){

            p.answer = ans;
            p.timeToAnswer = (new Date().getTime() - p.timeOfReceival)/1000.0;
            System.out.println("it took user " + p.timeToAnswer + " seconds to answer");//debug

        }
    }

    /**
     * Gets called when the server receives an emote from a player.
     *
     * @param emote The emote that should be added to the game.
     */
    public void addEmote(Emote emote) throws InterruptedException {
        Game game = gameRepository.getId(emote.gameId);
        game.emotes.add(emote);
        for (Player player : game.players) {
            GameState state = game.getState();
            state.instruction = "updateEmotes";
            sendToPlayer(player.id, state);
        }
        new Thread(() -> {
            try {
                Thread.sleep(5_000);
                game.emotes.remove(emote);
                for (Player player : game.players) {
                    GameState state = game.getState();
                    state.instruction = "updateEmotes";
                    sendToPlayer(player.id, state);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * Scores all players of a game at the end of the QUESTION phase of a gameState. Increases their score
     * depending on the correctness and speed of their answer.
     *
     * @param g The game that is scored.
     */
    public void score(Game g) {
        System.out.println("Score function has been called:");
        Question q = g.questions.get(g.currentQuestion);
        for (Player p : g.players) {
            double finalAdd = 0;
            if(q.type.equals("3")){//Open-ended
                System.out.println("this question is open ended");//debug
                boolean inFunctionShouldReceive = false;//check if double points was used
                if (p.shouldReceiveDouble) {
                    p.shouldReceiveDouble = false;
                    inFunctionShouldReceive = true;
                    System.out.println("DOUBLE POINTS POWER UP TOOK PLACE IN SCORING");
                }
                if (p.answer != null) {

                    double pAnswer = 0;
                    try {
                        pAnswer = Double.parseDouble(p.answer);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    double qAnswer = Double.parseDouble(q.answer);
                    double difference = Math.abs(pAnswer - qAnswer);
                    System.out.println("Player with id " + p.id + "has this answer " + pAnswer + " and the question answer is " + q.answer);
                    double toAdd = 0;
                    if(difference < qAnswer/2){//award points only in this case
                        System.out.println("player with id " + p.id + " will get awarded for the open question");
                        double maxPoints = 10.0 - p.timeToAnswer;
                        System.out.println("max points user should get are " + maxPoints);
                        double ratio = (qAnswer - difference)/qAnswer;
                        System.out.println("the ratio is " + ratio);
                        toAdd = ratio*maxPoints;
                        if (inFunctionShouldReceive) {
                            toAdd = toAdd*2;
                        }
                        System.out.println("toadd is " + toAdd);
                        finalAdd = toAdd;
                        p.score = p.score + toAdd * 10;
                    }
                    System.out.println("Player with id " + p.id + " won that many points - " + toAdd);
                }
            }else {//Multiple Choice
                System.out.println("this question is MC");//debug
                boolean inFunctionShouldReceive = false;//check if double points was used
                if (p.shouldReceiveDouble) {
                    p.shouldReceiveDouble = false;
                    inFunctionShouldReceive = true;
                    System.out.println("DOUBLE POINTS POWER UP TOOK PLACE IN SCORING");
                }
                double toAdd = 0;
                if (p.answer != null && p.answer.equals(q.answer)) {
                    toAdd = 10.0 - p.timeToAnswer;
                    if (inFunctionShouldReceive) {
                        toAdd = toAdd * 2;
                    }
                    System.out.println("ANSWER IS CORRECT");
                    finalAdd = toAdd;
                    p.score = p.score + toAdd * 10;
                }
                System.out.println("Player with id " + p.id + " won that many points - " + toAdd);
            }
            p.answer = null;
            GameState state = g.getState();
            state.instruction = "score";
            state.thisScored = finalAdd;
            sendToPlayer(p.id, state);
        }
    }
}
