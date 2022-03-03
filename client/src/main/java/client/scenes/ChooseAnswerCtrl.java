package client.scenes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.inject.Inject;

public class ChooseAnswerCtrl {

    @FXML
    public static AnchorPane AnchorPane1;

    @FXML
    private static Button Button1;

    @FXML
    private static MenuBar MenuBar1;

    @FXML
    private static VBox VBox1;

//    public static AnchorPane getAnchorPane1() {
//        return AnchorPane1;
//    }

    @FXML
    void Button1Pressed(ActionEvent event) {
        System.out.println("e ok");
    }
}

