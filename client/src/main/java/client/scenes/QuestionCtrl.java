package client.scenes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Communication.AnswerCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class QuestionCtrl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

    @FXML
    private Button doublePoints;

    @FXML
    private Button fiftyFifty;

    @FXML
    private Button halfTime;


    @FXML
    private ImageView questionImage;

    @FXML
    private Label questionText;

    @FXML
    private Label questionTitle;

    @FXML
    void Answer1Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(answer1.getText());
    }

    @FXML
    void Answer2Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(answer2.getText());
    }

    @FXML
    void Answer3Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(answer3.getText());
    }

    @FXML
    void initialize() {
        assert questionImage != null : "fx:id=\"questionImage\" was not injected: check your FXML file 'Question.fxml'.";
        assert questionText != null : "fx:id=\"questionText\" was not injected: check your FXML file 'Question.fxml'.";
        assert questionTitle != null : "fx:id=\"questionTitle\" was not injected: check your FXML file 'Question.fxml'.";

    }

}