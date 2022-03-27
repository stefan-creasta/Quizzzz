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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private AddPlayerCtrl playerCtrl;
    private Scene player;

    private LobbyCtrl lobbyCtrl;
    private Scene lobby;

    private CountdownTimer timerCtrl;
    private Scene timer;

    private GameCommunication gameCommunication;

    private QuestionCtrl questionCtrl;
    private Scene question;

    private ServerListener serverListener;

    private long gameId;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<QuestionCtrl, Parent> question,
                           Pair<CountdownTimer, Parent> timer,
                           Pair<LobbyCtrl, Parent> lobbyPair,
                           Pair<AddPlayerCtrl, Parent> playerPair,
                           GameCommunication gameCommunication,
                           ServerListener serverListener) {

        this.gameCommunication = gameCommunication;
        this.serverListener = serverListener;

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.timerCtrl = timer.getKey();
        this.timer = new Scene(timer.getValue());


        this.questionCtrl = question.getKey();
        gameId = gameCommunication.createGame();
        this.question = new Scene(question.getValue());

        this.lobbyCtrl = lobbyPair.getKey();
        this.lobby = new Scene(lobbyPair.getValue());

        this.playerCtrl = playerPair.getKey();
        this.player = new Scene(playerPair.getValue());

        showPlayer();
        //showQuestion();
        primaryStage.show();
    }

    public void joinGame(String username) {
        GameState state = gameCommunication.joinGame(gameId, username);
        handleGameState(state);
        serverListener.initialize(state.playerId, this);
    }

    public boolean checkUsername(String username) throws IOException, InterruptedException {
        return gameCommunication.checkUsername(gameId, username);
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showLobby() throws IOException, InterruptedException {
        primaryStage.setTitle("Lobby");
        primaryStage.setScene(lobby);
        lobbyCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
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
    
    public List<String> getPlayers() throws IOException, InterruptedException {
        return gameCommunication.getPlayers(gameId);
    }

    /**
     * Sends a request to the server to initiate the game with ID gameId
     */
    public void initiateGame() {
        gameCommunication.initiateGame(gameId);
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
            case "questionPhase"://called at the start of a question phase
                questionCtrl.updateGameState(gameState);
                showQuestion();
                questionCtrl.clearAnswer();
                questionCtrl.setQuestion(gameState.question);
                break;
            case "intervalPhase"://called at the start of an interval phase
                questionCtrl.markAnswer(gameState.question.answer, gameState.playerAnswer);
                break;
            case "answerSubmitted":
                break;

        }

    }
}