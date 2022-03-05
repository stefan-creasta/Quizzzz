package client.scenes;

import client.Communication.AnswerCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ChooseAnswerCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    public Button Button1;

    @FXML
    public Button Button2;

    @FXML
    public Button Button3;

    @Inject
    public ChooseAnswerCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    void Button1Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(Button1.getText());
    }

    @FXML
    void Button2Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(Button2.getText());
    }

    @FXML
    void Button3Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(Button3.getText());
    }
}

