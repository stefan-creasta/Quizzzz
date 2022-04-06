package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    public void quit(ActionEvent actionEvent) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }

}
