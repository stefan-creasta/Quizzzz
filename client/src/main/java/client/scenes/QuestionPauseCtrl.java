package client.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class QuestionPauseCtrl {
    @FXML
    private Rectangle timeBar;

    private Timeline timeline;

    public QuestionPauseCtrl() {}

    public void initialize() {
        double endWidth = timeBar.getWidth();
        this.timeline= new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(timeBar.widthProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(timeBar.widthProperty(), endWidth))
        );
    }

    public void showAnimation() {
        timeline.jumpTo(Duration.seconds(0));
        timeline.play();
    }
}
