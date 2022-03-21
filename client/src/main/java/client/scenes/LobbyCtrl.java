package client.scenes;

import client.Communication.LobbyCommunication;
import client.Communication.PlayerCommunication;
import com.google.inject.Inject;
import commons.Lobby;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbyCtrl implements Initializable {

    private static PlayerCommunication playerCommunication;
    private final MainCtrl mainCtrl;


    private Lobby currentLobby;
    private ObservableList<Player>  playerlist;


    @FXML
    private Button startButton;
    @FXML
    private Button leaveButton;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> col1;
    @FXML
    private TableColumn<Player, String> col2;

    @Inject
    public LobbyCtrl(PlayerCommunication playerCommunication, MainCtrl mainCtrl) {
        this.playerCommunication = playerCommunication;
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
//        col2.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
//        currentLobby = new Lobby();
    }

    public void refresh() {
        System.out.println("REFRESHING LOBBY");
        var players = playerCommunication.getPlayers();
        playerlist = FXCollections.observableList(players);
        table.setItems(playerlist);
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).username);
        }
    }

    public void leave() {
        System.out.println("Leaving");
    }


    @FXML
    public void startGame() throws IOException, InterruptedException {
        LobbyCommunication lobbyCommunication = new LobbyCommunication();
        lobbyCommunication.removePlayersFromLobby();
        mainCtrl.initiateGame();
        mainCtrl.showQuestion();
        //System.out.println(lobbyCommunication.getPlayers().size());
    }
}
