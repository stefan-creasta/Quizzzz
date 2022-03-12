package client.scenes;

import client.Communication.AnswerCommunication;
import client.Communication.ImageCommunication;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

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

    @FXML
    public ImageView imageview;

    @Inject
    public ChooseAnswerCtrl(ServerUtils server, MainCtrl mainCtrl) throws IOException, InterruptedException {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize() {
        this.imageview.setImage(ImageCommunication.getImage("http://localhost:8080/images/image%202.png"));
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

