package client.scenes;

import client.Communication.PlayerCommunication;
import com.google.inject.Inject;
import commons.Lobby;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyCtrl {

    private final PlayerCommunication playerCommunication;
    private final MainCtrl mainCtrl;
    private Lobby currentLobby;
    private List<Player> playerlist;

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

    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        col2.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        currentLobby = new Lobby();
    }

    public void addPlayer(Player newPlayer) {
        playerCommunication.addPlayer(newPlayer);
    }

    public List<Player> getPlayers() {
            return playerCommunication.getPlayers();
    }

}
