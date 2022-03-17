package client.scenes;
import java.net.URL;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.util.Duration;
import commons.Timer;

public class CountdownTimer implements Initializable {
    private Timer timer;
    @FXML
    private Text questionTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timer = new Timer(0,5);
        Timeline timeline= new Timeline( new KeyFrame(Duration.millis(1),e ->{
            questionTime.setText(timer.toTimerDisplayString());
        }));
        timeline.setCycleCount((int)timer.getDurationLong()/1000);
        timeline.play();


    }
}