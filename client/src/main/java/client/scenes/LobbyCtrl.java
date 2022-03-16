package client.scenes;

import client.utils.ServerUtils;
import commons.Lobby;
import commons.Player;
import commons.Quote;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.google.inject.Inject;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class LobbyCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Lobby currentLobby;

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
    public LobbyCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        col2.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
        currentLobby = new Lobby();
    }

    public void addPlayer() {
        mainCtrl.showPlayer();
    }

}
