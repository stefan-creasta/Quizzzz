package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class SplashScreenCtrl {
    private MainCtrl mainCtrl;

    @FXML
    private Button quitButton;

    @Inject
    public SplashScreenCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void chooseSingleplayer() {
        mainCtrl.chooseSingleplayer();
    }
    public void chooseMultiplayer() {
        mainCtrl.chooseMultiplayer();
    }

    public void adminPanel(ActionEvent actionEvent) {
        mainCtrl.showAdminInterface();
    }

    public void exitGame(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit game");
        alert.setHeaderText("You're about to exit the game!");
        alert.setContentText("Are you sure you want to close the application?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            mainCtrl.exitGame();
            stage.close();
        }
    }

}
