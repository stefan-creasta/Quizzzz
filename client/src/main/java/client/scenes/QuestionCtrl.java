package client.scenes;

import client.Communication.AnswerCommunication;
import client.Communication.ImageCommunication;
import client.Communication.PowerUpsCommunication;
import commons.*;
import commons.Timer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static javafx.beans.binding.Bindings.createObjectBinding;

public class QuestionCtrl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button answer1;

    @FXML
    private Button answer2;

    @FXML
    private Button answer3;

    @FXML
    private Button emoteAngry;

    @FXML
    private Button emoteLOL;

    @FXML
    private Button emoteSweat;

    @FXML
    private Button emoteClap;

    @FXML
    private Button emoteWin;

    @FXML
    private Button doublePoints;

    @FXML
    private Button eliminateWrongAnswer;

    @FXML
    private Button halfTime;

    @FXML
    private ImageView questionImage;

    @FXML
    private Label questionText;

    @FXML
    private Label questionTitle;

    @FXML
    private Label questionTime;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;

    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardUsernames;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardRanks;
    @FXML
    private TableColumn<LeaderboardEntry, String> leaderboardScores;

    @FXML
    private TableView<EmoteEntry> emotes;

    @FXML
    private TableColumn<EmoteEntry, String> emotesUsernameColumn;

    @FXML
    private TableColumn<EmoteEntry, String> emotesEmoteColumn;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField answerTextBox;

    @FXML
    private ProgressBar timeBar;

    @FXML
    private Label scoreLabel;

    private Boolean[] pressedEmote = {false, false, false, false, false};

    private Timer timer;

    private GameState gameState;

    private String selectedAnswer;

    private Timeline timeline;

    public boolean halfTimeWasUsed = false;

    private MainCtrl mainCtrl;

    @Inject
    public QuestionCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Updates the gameState for this particular client
     *
     * @param gameState the new gameState
     */
    void updateGameState(GameState gameState) {
        this.gameState = gameState;
        switch(gameState.instruction){
            case "questionPhase":
                timeBar.setVisible(true);
                questionTime.setVisible(true);
                scoreLabel.setVisible(false);
                //TODO: Update question number based on current question
                this.questionTitle.setText("Question 10");
                this.questionText.setText(gameState.question.question);

                if(!gameState.question.type.equals("3")){
                    this.answer1.setText(gameState.question.answer);
                    this.answer2.setText(gameState.question.wrongAnswer1);
                    this.answer3.setText(gameState.question.wrongAnswer2);
                    answer1.setVisible(true);
                    answer2.setVisible(true);
                    answer3.setVisible(true);
                }else{
                    answerTextBox.clear();
                    answerTextBox.setVisible(true);
                }
                
                timeline = new Timeline( new KeyFrame(Duration.millis(1), e ->{
                    double timeToDisplay = 10000 - (new Date().getTime() - gameState.timeOfReceival);
                    questionTime.setText("Time left: " + String.format("%.3f", timeToDisplay/1000.0) + " seconds");
                    timeBar.setProgress(timeToDisplay/10000.0);
                }));
                timeline.setCycleCount(10000);
                timeline.play();

                break;
            case "halfTimePowerUp":
                timeline = new Timeline( new KeyFrame(Duration.millis(1), e ->{
                    double timeToDisplay = 10000 - (new Date().getTime() - gameState.timeOfReceival);
                    if(timeToDisplay < 0){
                        questionTime.setText("Time left: 0.000 seconds");
                        timeBar.setProgress(0);
                    }else{
                        questionTime.setText("Time left: " + String.format("%.3f", timeToDisplay / 1000.0) + " seconds");
                        timeBar.setProgress(timeToDisplay / 10000.0);
                    }

                }));
                timeline.setCycleCount(10000);
                timeline.play();
                //TODO time is already being halved, but make it explicit to the client, so that it is easily noticeable
                break;
            case "score":
                timeBar.setVisible(false);
                questionTime.setVisible(false);
                scoreLabel.setText("You received: " + String.format("%.2f", gameState.thisScored) + " points!");
                scoreLabel.setVisible(true);
        }




    }

    //TODO: Send correct Game ID
    @FXML
    void Answer1Pressed(ActionEvent event) throws IOException, InterruptedException {
        selectedAnswer = answer1.getText();
    }

    //TODO: Send correct Game ID
    @FXML
    void Answer2Pressed(ActionEvent event) throws IOException, InterruptedException {
        selectedAnswer = answer2.getText();
    }

    //TODO: Send correct Game ID
    @FXML
    void Answer3Pressed(ActionEvent event) throws IOException, InterruptedException {
        selectedAnswer = answer3.getText();
    }

    @FXML
    /**
     * Gets called when the submit answer button is pressed
     * Sends the answer to the server, together with the gameState
     */
    public void SubmitPressed(ActionEvent actionEvent) throws IOException, InterruptedException {
        if(answer1.isVisible()){//if we have an MC question
            AnswerCommunication.sendAnswer(selectedAnswer, gameState, mainCtrl.playerCtrl.serverString);
        }else if(answerTextBox.isVisible()){//if we have an open question
            selectedAnswer = answerTextBox.getText();
            AnswerCommunication.sendAnswer(selectedAnswer, gameState, mainCtrl.playerCtrl.serverString);
        }else{
            throw new IllegalArgumentException("we messed up the submitPressed function logic :(");
        }

    }

    /**
     * Gets called when the Angry emote is pressed.
     * Sends the emote to the server and stops further identical emotes until the next question.
     */
    @FXML
    void AngryEmotePressed(ActionEvent event) throws IOException, InterruptedException {
        if (!pressedEmote[0]) {
            Emote emote = new Emote(Emote.Type.Angry, gameState.username, gameState.gameId);
            AnswerCommunication.sendEmote(emote, mainCtrl.playerCtrl.serverString);
            pressedEmote[0] = true;
        }
    }

    /**
     * Gets called when the LOL emote is pressed.
     * Sends the emote to the server and stops further identical emotes until the next question.
     */
    @FXML
    void LOLEmotePressed(ActionEvent event) throws IOException, InterruptedException {
        if (!pressedEmote[1]) {
            Emote emote = new Emote(Emote.Type.LOL, gameState.username, gameState.gameId);
            AnswerCommunication.sendEmote(emote, mainCtrl.playerCtrl.serverString);
            pressedEmote[1] = true;
        }
    }

    /**
     * Gets called when the Sweat emote is pressed.
     * Sends the emote to the server and stops further identical emotes until the next question.
     */
    @FXML
    void SweatEmotePressed(ActionEvent event) throws IOException, InterruptedException {
        if (!pressedEmote[2]) {
            Emote emote = new Emote(Emote.Type.Sweat, gameState.username, gameState.gameId);
            AnswerCommunication.sendEmote(emote, mainCtrl.playerCtrl.serverString);
            pressedEmote[2] = true;
        }
    }

    /**
     * Gets called when the Clap emote is pressed.
     * Sends the emote to the server and stops further identical emotes until the next question.
     */
    @FXML
    void ClapEmotePressed(ActionEvent event) throws IOException, InterruptedException {
        if (!pressedEmote[3]) {
            Emote emote = new Emote(Emote.Type.Clap, gameState.username, gameState.gameId);
            AnswerCommunication.sendEmote(emote, mainCtrl.playerCtrl.serverString);
            pressedEmote[3] = true;
        }
    }

    /**
     * Gets called when the Win emote is pressed.
     * Sends the emote to the server and stops further identical emotes until the next question.
     */
    @FXML
    void WinEmotePressed(ActionEvent event) throws IOException, InterruptedException {
        if (!pressedEmote[4]) {
            Emote emote = new Emote(Emote.Type.Win, gameState.username, gameState.gameId);
            AnswerCommunication.sendEmote(emote, mainCtrl.playerCtrl.serverString);
            pressedEmote[4] = true;
        }
    }


    @FXML
    /**
     * Gets called when the double points power up gets activated. Sends a request to the server,
     * where it is checked if the power up has already been used by this client. If not, the power
     * up is used and all players are alerted.
     */
    public void DoublePointsButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        String result = PowerUpsCommunication.sendPowerUps("doublePointsPowerUp", gameState, mainCtrl.playerCtrl.serverString);

        try {
            if (result.split("___")[1].equals("success")) {
                //TODO tell user that the powerup has been used properly
            } else {
                System.out.println("doublePointsPowerUp has already been used before");
                //TODO tell user power up is already used
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    @FXML
    /**
     * Gets called when the eliminate wrong answer power up is used. Sends a request to the server,
     * where it is checked if the player has already used that power up. If not, the power up is used,
     * and the wrong answer button is made invisible and inaccessible. Also other players are alerted.
     */
    void EliminateWrongAnswerButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        String result = PowerUpsCommunication.sendPowerUps("eliminateWrongAnswerPowerUp", gameState, mainCtrl.playerCtrl.serverString);

        try {
            if (result.split("___")[1].equals("success")) {
                String answer = result.split("___")[2];
                if (answer1.getText().equals(answer)) {
                    setVisible("answer1", false);//TODO dont forget to turn visible later if needed
                }
                if (answer2.getText().equals(answer)) {
                    setVisible("answer2", false);//TODO dont forget to turn visible later if needed
                }
                if (answer3.getText().equals(answer)) {
                    setVisible("answer3", false);//TODO dont forget to turn visible later if needed
                }

            }
            if (result.split("___")[1].equals("fail")) {
                System.out.println("eliminateWrongAnswerPowerUp has already been used before");
                //TODO tell user that power up has already been used before

            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }

    @FXML
    /**
     * Gets called when the half time power up is used. Sends a request to the server,
     * where it is checked if the player has already used that power up. If not, the power up is used,
     * and the time of all other players is halved.//TODO finish
     */
    void HalfTimeButtonPressed(ActionEvent event) throws IOException, InterruptedException {
        String result = PowerUpsCommunication.sendPowerUps("halfTimePowerUp", gameState, mainCtrl.playerCtrl.serverString);
        try {
            if (result.split("___")[1].equals("success")) {
                System.out.println("halftime has been used but maybe not implemented yet");
                //TODO show to user that power up is used
            } else {
                System.out.println("halfTimePowerUp has already been used before");
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }


    }

    @FXML
    void KeyPressed(KeyEvent event) {
        System.out.println(event.getCode() + " was pressed.");
        switch (event.getCode()) {
            case TAB:
                showLeaderboard();
        }
    }

    @FXML
    void KeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case TAB:
                hideLeaderboard();
        }
    }

    @FXML
    void initialize() {
        assert questionImage != null : "fx:id=\"questionImage\" was not injected: check your FXML file 'Question.fxml'.";
        assert questionText != null : "fx:id=\"questionText\" was not injected: check your FXML file 'Question.fxml'.";
        assert questionTitle != null : "fx:id=\"questionTitle\" was not injected: check your FXML file 'Question.fxml'.";


        root.addEventFilter(KeyEvent.KEY_PRESSED, this::KeyPressed);
        root.addEventFilter(KeyEvent.KEY_RELEASED, this::KeyReleased);

        timer = new Timer(0, 5);

        leaderboardRanks.setCellFactory(e -> {
            TableCell<LeaderboardEntry, String> indexCell = new TableCell<>();
            var rowProperty = indexCell.tableRowProperty();
            var rowBinding = createObjectBinding(() -> {
                TableRow<LeaderboardEntry> row = rowProperty.get();
                if (row != null) {
                    int rowIndex = row.getIndex();
                    if (rowIndex < row.getTableView().getItems().size()) {
                        return "#" + Integer.toString(rowIndex + 1);
                    }
                }
                return null;
            }, rowProperty);
            indexCell.textProperty().bind(rowBinding);
            return indexCell;
        });
        leaderboardUsernames.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().username));
        leaderboardScores.setCellValueFactory(e -> new SimpleStringProperty(Integer.toString(e.getValue().score)));


        emotesUsernameColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().username));
        emotesEmoteColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().type));
        emotes.setPlaceholder(new Label(""));
        hideLeaderboard();
    }


    /**
     * Makes an FXML element visible/invisible
     *
     * @param variableName the fxml ID of the element
     * @param should       whether the element should become visible, or invisible
     */
    public void setVisible(String variableName, boolean should) {
        switch (variableName) {
            case "answer1":
                answer1.setVisible(should);
                break;
            case "answer2":
                answer2.setVisible(should);
                break;
            case "answer3":
                answer3.setVisible(should);
                break;
        }
    }

    public void syncTimer(long syncLong, long duration) {
        timer.setDuration(duration);
        timer.synchronize(syncLong);
    }



    /**
     * Sets the new Question for the Question phase.
     * @param q the new Question
     */
    public void setQuestion(Question q, String serverString) {
        questionText.setText(q.question);//sets the question Text

        if (q.questionImage != null) {//sets the Image
            try {
                System.out.println("serverString is: " + serverString);
                System.out.println("serverString is: " + serverString);
                System.out.println("serverString is: " + serverString);
                questionImage.setImage(ImageCommunication.getImage(serverString + "/" + q.questionImage));
            }
            catch (IOException e) {
                System.out.println("Failed to set the question image.");
            }
        }

        try{
            if(!q.type.equals("3")){//if this is a MC question
                List<String> answerList = new LinkedList<>(List.of(q.answer, q.wrongAnswer1, q.wrongAnswer2));
                Collections.shuffle(answerList);
                clearAnswer();
                answer1.setText(answerList.get(0));
                answer2.setText(answerList.get(1));
                answer3.setText(answerList.get(2));
                answerTextBox.setVisible(false);
            }else{//if this is an open question
                answer1.setVisible(false);
                answer2.setVisible(false);
                answer3.setVisible(false);
                clearAnswer();
                answerTextBox.setVisible(true);
            }
        }
        catch(Exception exc){
            exc.printStackTrace();
            System.out.println("null pointer again????");
        }

        for (int i=0; i<5; i++){//Reset the emotes so they can be pressed again for the new question.
            pressedEmote[i] = false;
        }

    }

    public void markAnswer(String correct, String ofplayer, String type) {
        if (type.equals("3")) {
            answerTextBox.setText(correct);
        } else {
            for (Button answer : List.of(answer1, answer2, answer3)) {
                answer.getStyleClass().removeAll("wrong", "right", "default");
                if (answer.getText().equals(correct)) {
                    answer.getStyleClass().add("right");
                } else if (answer.getText().equals(ofplayer)) {
                    answer.getStyleClass().add("wrong");
                } else {
                    answer.getStyleClass().add("default");
                }
            }
        }
    }

    public void clearAnswer() {
        for (Button answer : List.of(answer1, answer2, answer3)) {
            answer.getStyleClass().removeAll("wrong", "right", "default");
            answer.getStyleClass().add("default");
        }
    }

    public void showLeaderboard() {
        List<LeaderboardEntry> leaderboardEntries = new LinkedList<LeaderboardEntry>();
        leaderboardEntries.addAll(List.of(
                new LeaderboardEntry("userA", 300),
                new LeaderboardEntry("userC", 100),
                new LeaderboardEntry("userB", 200)
        ));
        Collections.sort(leaderboardEntries);

        ObservableList<LeaderboardEntry> entries = FXCollections.observableList(leaderboardEntries);
        leaderboard.setItems(entries);
        leaderboard.setVisible(true);
    }

    public void hideLeaderboard() {
        leaderboard.setVisible(false);
    }

    public void updateEmotes(List<Emote> emoteEntries) {
        //TODO: Fix cell so that the image of the emote is displayed
        List<EmoteEntry> emoteEntriesWithImage = new ArrayList<>();
        for(Emote emote : emoteEntries) {

            emoteEntriesWithImage.add(new EmoteEntry(emote.username, String.valueOf(emote.type)));
        }
        System.out.println(emoteEntriesWithImage);
        ObservableList<EmoteEntry> emoteEntriesList = FXCollections.observableList(emoteEntriesWithImage);
        this.emotes.setItems(emoteEntriesList);
    }
}

class EmoteEntry {

    public String username;
    public String type;

    public EmoteEntry(String username, String type) {
        this.username = username;
        this.type = type;
    }

}
