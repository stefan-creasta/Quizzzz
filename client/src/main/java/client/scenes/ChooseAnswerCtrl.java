package client.scenes;

import client.Communication.AnswerCommunication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ChooseAnswerCtrl<Button1> {

    @FXML
    public static AnchorPane AnchorPane1;

    @FXML
    public static Button Button1;

    @FXML
    public static Button Button2;

    @FXML
    public static Button Button3;

    @FXML
    private static MenuBar MenuBar1;

    @FXML
    private static VBox VBox1;


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


    //presetting the text inside the button TODO - FIX
    public void initialize(){
        Button1 = new Button();
        Button1.setText("Ans1");
        Button2 = new Button();
        Button2.setText("Ans2");
        Button3 = new Button();
        Button3.setText("Ans3");
    }
}

