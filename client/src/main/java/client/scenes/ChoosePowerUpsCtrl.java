package client.scenes;

import client.Communication.PowerUpsCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ChoosePowerUpsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    public Button DoublePointsButton;

    @FXML
    public Button EliminateWrongAnswerButton;

    @FXML
    public Button HalfTimeButton;

    @Inject
    public ChoosePowerUpsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    void DoublePointsButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        PowerUpsCommunication.sendPowerUps(DoublePointsButton.getText() + " WAS USED!");
    }

    @FXML
    void EliminateWrongAnswerButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        PowerUpsCommunication.sendPowerUps(EliminateWrongAnswerButton.getText()+ " WAS USED!");
    }

    @FXML
    void HalfTimeButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        PowerUpsCommunication.sendPowerUps(HalfTimeButton.getText() +" WAS USED!");
    }
}

