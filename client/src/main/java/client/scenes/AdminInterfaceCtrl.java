package client.scenes;

import client.Communication.ImporterCommunication;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class AdminInterfaceCtrl {
    @FXML
    private TextField pathField;

    private MainCtrl mainCtrl;

    private ImporterCommunication importerCommunication;

    @Inject
    public AdminInterfaceCtrl(ImporterCommunication communication, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.importerCommunication = communication;
    }

    public void back(ActionEvent actionEvent) {
        mainCtrl.showSplashScreen();
    }

    public void importQuestions(ActionEvent actionEvent) {
        String message = importerCommunication.importQuestions(pathField.getText());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Result");
        alert.setContentText(message);
        alert.show();
    }
}
