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
import client.Communication.PlayerCommunication;
import client.Communication.ServerListener;
import com.google.inject.Inject;
import commons.GameState;
import commons.Player;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class AddPlayerCtrl {

    private final PlayerCommunication server;
    private final MainCtrl mainCtrl;
    private final ServerListener serverListener;
    private final GameCommunication gameCommunication;

    @FXML
    private TextField usernameField;

    @Inject
    public AddPlayerCtrl(PlayerCommunication server, MainCtrl mainCtrl, ServerListener serverListener, GameCommunication gameCommunication) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.serverListener = serverListener;
        this.gameCommunication = gameCommunication;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showLobby();
    }

    public void play() {
        try {
            Player newPlayer = getPlayer();
            server.addPlayer(newPlayer);
            mainCtrl.joinGame(newPlayer.id);

        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        clearFields();
        mainCtrl.showLobby();
    }

    private Player getPlayer() {
        var username = usernameField.getText();
        return new Player(username, 0);
    }

    private void clearFields() {
        usernameField.clear();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                play();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }
}