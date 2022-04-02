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
import com.google.inject.Inject;
import commons.Player;
import commons.Question;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.io.IOException;

public class AddPlayerCtrl {

    private final MainCtrl mainCtrl;
    private final ServerListener serverListener;
    private final GameCommunication gameCommunication;
    public String serverString = "not_initialized";

    @FXML
    private TextField usernameField;
    @FXML
    private TextField serverField;
    @FXML
    private Label addressLabel;

    @Inject
    public AddPlayerCtrl(MainCtrl mainCtrl, ServerListener serverListener, GameCommunication gameCommunication) {
        this.mainCtrl = mainCtrl;
        this.serverListener = serverListener;
        this.gameCommunication = gameCommunication;
    }

    /**
     * Gets called when a player decides to go back to the main screen by pressing ESCAPE or a button that will lead
     * there, that is not implemented yet.
     * @throws IOException can be thrown
     * @throws InterruptedException can be thrown
     */
    public void cancel() throws IOException, InterruptedException {
        clearFields();
        mainCtrl.showSplashScreen();
    }

    public void play() throws IOException, InterruptedException {
        // here instead of in the multplayer clause
        Player newPlayer = getPlayer();
        //if the game is singleplayer, then the game can start
        if(mainCtrl.singleplayerGame) {

            serverString = "http://localhost:8080";
            mainCtrl.initiateSingleplayerGame(newPlayer);
            mainCtrl.showQuestion();
            serverField.setVisible(true);
            addressLabel.setVisible(true);
        }
        else {
            serverString = serverField.getText();
            if (mainCtrl.checkUsername(newPlayer.username)) {
                try {
                    mainCtrl.joinGame(newPlayer.username);

                } catch (WebApplicationException e) {

                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                    return;
                }
                mainCtrl.showLobby();
            }
        }
    }

    private Player getPlayer() {
        var username = usernameField.getText();
        clearFields();
        return new Player(username, 0);
    }

    private void clearFields() {
        usernameField.clear();
    }

    public void keyPressed(KeyEvent e) throws IOException, InterruptedException {
        switch (e.getCode()) {
            case ENTER:
                play();
                break;
            case ESCAPE:
                serverField.setVisible(true);
                addressLabel.setVisible(true);
                cancel();
                break;
            default:
                break;
        }
    }

    public void invisServerField(){
        serverField.setVisible(false);
        addressLabel.setVisible(false);
    }
}