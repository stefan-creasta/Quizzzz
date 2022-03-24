package client.scenes;

import com.google.inject.Inject;
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
import java.util.ResourceBundle;

public class LobbyCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    private ObservableList<String>  playerlist;


    @FXML
    private Button startButton;
    @FXML
    private Button leaveButton;
    @FXML
    private TableView<String> table;
    @FXML
    private TableColumn<Player, String> col1;
    @FXML
    private TableColumn<Player, String> col2;

    @Inject
    public LobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        col1.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
//        col2.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().username));
//        currentLobby = new Lobby();
    }

    public void refresh() throws IOException, InterruptedException {
        System.out.println("REFRESHING LOBBY");
        var players = mainCtrl.getPlayers();
        System.out.println(players.size());
        playerlist = FXCollections.observableList(players);
        table.setItems(playerlist);
    }

    public void leave() {
        System.out.println("Leaving");
    }


    @FXML
    public void startGame() throws IOException, InterruptedException {
        mainCtrl.initiateGame();
        mainCtrl.showQuestion();
        //System.out.println(lobbyCommunication.getPlayers().size());
    }
}
