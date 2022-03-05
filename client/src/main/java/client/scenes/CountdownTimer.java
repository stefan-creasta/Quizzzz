package client.scenes;
import java.net.URL;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CountdownTimer implements Initializable {
    int i = 20;
    @FXML
    private Text questionTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Timeline timeline= new Timeline( new KeyFrame(Duration.seconds(1),e ->{
            i--;
            questionTime.setText(String.valueOf(i));
        }));
        timeline.setCycleCount(20);
        timeline.play();

    }
}