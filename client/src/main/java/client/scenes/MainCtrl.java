/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Communication.GameCommunication;
import client.Communication.ServerListener;
import commons.GameState;
import commons.LeaderboardEntry;
import commons.Player;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainCtrl {

    public boolean singleplayerGame;

    private Stage primaryStage;


    public AddPlayerCtrl playerCtrl;
    private Scene player;

    private LobbyCtrl lobbyCtrl;
    private Scene lobby;

    private CountdownTimer timerCtrl;
    private Scene timer;

    private GameCommunication gameCommunication;

    private QuestionCtrl questionCtrl;
    private Scene question;

    private SplashScreenCtrl splashCtrl;
    private Scene splash;

    private GameEndingCtrl gameEndingCtrl;
    private Scene gameEnding;

    private QuestionPauseCtrl questionPauseCtrl;
    private Scene questionPause;

    private Scene adminInterface;

    private ServerListener serverListener;

    private long gameId;

    private String currentUsername;

    public List<String> serverUrls;

    public void initialize(Stage primaryStage,
                           Pair<QuestionCtrl, Parent> question,
                           Pair<CountdownTimer, Parent> timer,
                           Pair<LobbyCtrl, Parent> lobbyPair,
                           Pair<AddPlayerCtrl, Parent> playerPair,
                           Pair<AdminInterfaceCtrl, Parent> adminInterfacePair,
                           Pair<GameEndingCtrl, Parent> gameEndingPair,
                           GameCommunication gameCommunication,
                           ServerListener serverListener,
                           Pair<SplashScreenCtrl, Parent> splashScreenPair, Pair<QuestionPauseCtrl, Parent> questionPausePair) {

        this.gameCommunication = gameCommunication;
        this.serverListener = serverListener;

        this.primaryStage = primaryStage;

        this.timerCtrl = timer.getKey();
        this.timer = new Scene(timer.getValue());

        this.questionCtrl = question.getKey();
        this.question = new Scene(question.getValue());

        this.lobbyCtrl = lobbyPair.getKey();
        this.lobby = new Scene(lobbyPair.getValue());

        this.playerCtrl = playerPair.getKey();
        this.player = new Scene(playerPair.getValue());

        this.splashCtrl = splashScreenPair.getKey();
        this.splash = new Scene(splashScreenPair.getValue());

        this.gameEndingCtrl = gameEndingPair.getKey();
        this.gameEnding = new Scene(gameEndingPair.getValue());
        this.questionPauseCtrl = questionPausePair.getKey();
        this.questionPause = new Scene(questionPausePair.getValue());

        System.out.println("GAME ID: " + gameId);
        showSplashScreen();
        //showPlayer();
        this.adminInterface = new Scene(adminInterfacePair.getValue());

        readServerUrls();

        adminInterfacePair.getKey().registerServerUrlList(serverUrls);
        //showPlayer();
        //showQuestion();
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            exitGame(primaryStage);
        });
    }

    public void setupSingleplayerGame() {
        singleplayerGame = true;
        gameId = gameCommunication.createSingleplayerGame();
    }

    public boolean setupMultiplayerGame() {
        singleplayerGame = false;
        gameId = gameCommunication.createGame(playerCtrl.serverString);
        if(gameId==-1) return false;
        return true;
    }

    public void joinGame(String username) {
        GameState state = gameCommunication.joinGame(gameId, username, playerCtrl.serverString);
        handleGameState(state);
        serverListener.initialize(state.playerId, this, playerCtrl.serverString);
        currentUsername = username;
    }

    public boolean checkUsername(String username) throws IOException, InterruptedException {
        //In case the game that is being checked against is initiated in between username checks, accept the username
        // for the new game. Therefore, we are resetting the multiplayer gameId each time a username attempt is made.
        boolean flag = true;
        if (!singleplayerGame){
            flag = setupMultiplayerGame();
        }

        if(flag) {
            return gameCommunication.checkUsername(gameId, username, playerCtrl.serverString);
        }else{
            return false;
        }
    }

    public void showLobby() throws IOException, InterruptedException {
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(lobby);
        lobbyCtrl.refresh();
    }

    public void showSplashScreen() {
        primaryStage.setTitle("SplashScreen");
        primaryStage.setScene(splash);
    }
    public void chooseSingleplayer() {
        setupSingleplayerGame();
        playerCtrl.invisServerField();
        showPlayer();
    }
    public void chooseMultiplayer() {
//        setupMultiplayerGame();
        singleplayerGame = false;
        showPlayer();
    }
    public void showPlayer() {
        primaryStage.setTitle("Adding a player");
        primaryStage.setScene(player);
        player.setOnKeyPressed(e -> {
            try {
                playerCtrl.keyPressed(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void showTimer() {
        primaryStage.setTitle("Countdown Timer");
        primaryStage.setScene(timer);
    }

    public void showQuestion() {
        primaryStage.setTitle("Question");
        primaryStage.setScene(question);
    }

    private void showGameEnding() {
        primaryStage.setTitle("Game Over");
        primaryStage.setScene(gameEnding);
    }

    public void showQuestionPause() {
        primaryStage.setTitle("Pause");
        primaryStage.setScene(questionPause);
    }

    public void showAdminInterface() {
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(adminInterface);
    }
    
    public List<String> getPlayers() throws IOException, InterruptedException {
        return gameCommunication.getPlayers(gameId, playerCtrl.serverString);
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     *
     * @return the current list containing all the current entries for the current multiplayer game
     * @throws IOException
     * @throws InterruptedException
     */
    public List<LeaderboardEntry> getMultiplayerLeaderboards() throws IOException, InterruptedException{
        return gameCommunication.getLeaderboardMultiplayer(gameId, playerCtrl.serverString);
    }
    public List<LeaderboardEntry> getSingleplayerLeaderboards() throws IOException, InterruptedException{
        return gameCommunication.getLeaderboardSingleplayer();
    }
    
    public List<LeaderboardEntry> getServerLeaderboards() throws IOException, InterruptedException{
        return gameCommunication.getTopLeaderboard(playerCtrl.serverString);
    }
    public void updateServerLeaderboard(LeaderboardEntry e) throws IOException, InterruptedException{
            gameCommunication.addEntry(playerCtrl.serverString,e);
    }
    /**
     * Sends a request to the server to initiate the game with ID gameId
     */
    public void initiateGame() {
        gameCommunication.initiateGame(gameId, playerCtrl.serverString);
    }

    /**
     * Exits the current game i.e. breaks the server connection.
     */
    public void exitGame() {
        serverListener.stopListening();
    }

    /**
     * Sends a request to the server to initiate and create a singleplayer game
     */
    public void initiateSingleplayerGame(Player newPlayer) {
        gameId = gameCommunication.createSingleplayerGame();
        GameState state = gameCommunication.joinSingleplayerGame(gameId, newPlayer.username, playerCtrl.serverString);
        handleGameState(state);
        serverListener.initialize(state.playerId, this, playerCtrl.serverString);
        gameCommunication.initiateSingleplayerGame(gameId, playerCtrl.serverString);
        currentUsername = newPlayer.username;
    }



    /**
     * Function that gets called when the server is sending the player information using long polling.
     * It performs different actions depending on the instruction in the gameState. These actions
     * are handled using a switch case. If you use sendToPlayer() - the function that sends the gameState to this function -
     * then, before sending, set the instruction and add a switch case for the instruction here, if the case does not
     * exist already.
     * @param gameState the gameState with the updated information
     */
    public void handleGameState(GameState gameState) {
        String instruction = gameState.instruction;
        switch(instruction){
            case "halfTimePowerUp"://called when a halfTimePowerUp is being used.
                questionCtrl.updateGameState(gameState);
            case "updateEmotes":
                questionCtrl.updateEmotes(gameState.emotes);
                break;
            case "joinGame"://called when the client joins
                try {
                    showLobby();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "joinSingleplayerGame"://called when the client joins the singleplayer game
                break;
            case "questionPhase"://called at the start of a question phase
                questionCtrl.updateGameState(gameState);
                showQuestion();
                questionCtrl.clearAnswer();
                questionCtrl.setQuestion(gameState.question, playerCtrl.serverString);
                break;
            case "intervalPhase"://called at the start of an interval phase
                questionCtrl.markAnswer(gameState.question.answer, gameState.playerAnswer, gameState.question.type);
                break;
            case "pausePhase":
                showQuestionPause();
                break;
            case "endingPhase":
                showGameEnding();
                gameEndingCtrl.handleGameState(gameState);
                break;
            case "answerSubmitted":
                break;
            case "score":
                questionCtrl.updateGameState(gameState);
                break;
        }
    }

    /**
     * Method which exits the game. If the user is in the splash screen, it closes the app. Otherwise,
     * the method returns the user to the splash screen.
     * @param stage The current stage of the game
     */
    public void exitGame(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game");
        alert.setHeaderText("You're about to exit the game!");
        if(!stage.getScene().equals(this.splash)) {
            alert.setContentText("Are you sure you want to return to the splash screen?");
        }
        else {
            alert.setContentText("Are you sure you want to close the application?");
        }
        if(alert.showAndWait().get() == ButtonType.OK) {
            exitGame();
            if(stage.getScene().equals(this.splash)) {
                stage.close();
            }
            else {
                showSplashScreen();
            }
        }
    }

    /**
     * Read the server URLs from [working directory]\server-urls.txt.
     */
    public void readServerUrls() {
        try {
            File f = new File("server-urls.txt");
            f.createNewFile();
            String contents = Files.readString(f.toPath());
            serverUrls = new LinkedList<>(Arrays.asList(contents.split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
            serverUrls = new LinkedList<>(List.of("http://localhost:8080"));
        }
    }

    /**
     * Save the server URL both in the shared serverUrls list and in [working directory]\server-urls.txt.
     * @param s the URL to save
     */
    public void saveServerUrl(String s) {
        if (!serverUrls.contains(s)) {
            serverUrls.add(0, s);
            try {
                Files.writeString(new File("server-urls.txt").toPath(), s + "\n", StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}