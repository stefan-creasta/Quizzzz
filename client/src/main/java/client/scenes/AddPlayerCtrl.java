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
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AddPlayerCtrl {

    private final MainCtrl mainCtrl;
    private final ServerListener serverListener;
    private final GameCommunication gameCommunication;
    public String serverString = "not_initialized";

    @FXML
    private TextField usernameField;
    @FXML
    private TextField serverField;

    @Inject
    public AddPlayerCtrl(MainCtrl mainCtrl, ServerListener serverListener, GameCommunication gameCommunication) {
        this.mainCtrl = mainCtrl;
        this.serverListener = serverListener;
        this.gameCommunication = gameCommunication;
    }

    public void setUsername() {
        if (usernameField.getText().isBlank()) {
            usernameField.setText(readUsernameFromFile());
        }
    }

    /**
     * Gets called when a player decides to go back to the main screen by pressing ESCAPE or a button that will lead
     * there, that is not implemented yet.
     * @throws IOException can be thrown
     * @throws InterruptedException can be thrown
     */
    public void cancel() {
        mainCtrl.showSplashScreen();
    }

    public void play() throws IOException, InterruptedException {
        // here instead of in the multplayer clause
        Player newPlayer = getPlayer();
        //if the game is singleplayer, then the game can start
        if(mainCtrl.singleplayerGame) {
            if(!newPlayer.username.equals("")){
                writeUsernameToFile(newPlayer.username);
                serverString = "http://localhost:8080";
                mainCtrl.initiateSingleplayerGame(newPlayer);
                mainCtrl.showQuestionPause();
                serverField.setVisible(true);
            }else{
                Alert usernameAlertSingle = new Alert(Alert.AlertType.ERROR, "Please input a username");
                usernameAlertSingle.show();
            }
        }
        else {
            try {
                serverString = serverField.getText();
                if (mainCtrl.checkUsername(newPlayer.username) && newPlayer.username != null && !newPlayer.username.equals("")) {
                    writeUsernameToFile(newPlayer.username);
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
                } else {
                    clearUsernameField();
                    if (newPlayer.username.equals("")) {
                        Alert usernameAlert = new Alert(Alert.AlertType.ERROR, "Please input a username");
                        usernameAlert.show();
                    } else {
                        Alert usernameAlert = new Alert(Alert.AlertType.ERROR, "Username already taken");
                        usernameAlert.show();
                    }
                }
            }
            catch(IllegalArgumentException e){
                Alert usernameAlert = new Alert(Alert.AlertType.ERROR, "Server not valid");
                usernameAlert.show();
            }
        }
    }

    private Player getPlayer() {
        var username = usernameField.getText();
        return new Player(username, 0);
    }

    private void clearUsernameField() {
        usernameField.clear();
    }

    public void keyPressed(KeyEvent e) throws IOException, InterruptedException {
        switch (e.getCode()) {
            case ENTER:
                play();
                break;
            case ESCAPE:
                serverField.setVisible(true);
                cancel();
                break;
            default:
                break;
        }
    }

    public void invisServerField(){
        serverField.setVisible(false);
    }

    public void back(ActionEvent actionEvent) {
        mainCtrl.showSplashScreen();
        serverField.setVisible(true);
    }

    public void setServerUrl(String s) {
        serverString = s;
        serverField.setText(s);
    }

    public String readUsernameFromFile() {
        String s = "";
        try {
            new File("username.txt").createNewFile();
            s = Files.readString(Path.of("username.txt"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public boolean writeUsernameToFile(String s) {
        try {
            Files.writeString(Path.of("username.txt"), s);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}