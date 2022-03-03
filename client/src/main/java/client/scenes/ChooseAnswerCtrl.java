package client.scenes;

import client.Communication.AnswerCommunication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;

public class ChooseAnswerCtrl<Button1> {

    @FXML
    public static AnchorPane AnchorPane1;

    @FXML
    public static Button Button1;

    @FXML
    private static MenuBar MenuBar1;

    @FXML
    private static VBox VBox1;


    @FXML
    void Button1Pressed(ActionEvent event) throws IOException, InterruptedException {
        AnswerCommunication.sendAnswer(Button1.getText());
    }


    //presetting the text inside the button TODO - FIX
    public void initialize(){
        Button1 = new Button();
        Button1.setText("Ans");
    }
}

