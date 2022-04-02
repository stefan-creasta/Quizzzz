package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;

public class SplashScreenCtrl {
    private MainCtrl mainCtrl;

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
}
