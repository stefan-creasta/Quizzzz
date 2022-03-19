/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import commons.GameState;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
public class MainCtrl {

    private Stage primaryStage;

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private CountdownTimer timerCtrl;
    private Scene timer;



    private QuestionCtrl questionCtrl;
    private Scene question;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
            Pair<AddQuoteCtrl, Parent> add,
                           Pair<QuestionCtrl, Parent> question,
                           GameState gameState,
                           Pair<CountdownTimer,Parent> timer) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.timerCtrl  = timer.getKey();
        this.timer = new Scene(timer.getValue());


        this.questionCtrl = question.getKey();
        this.questionCtrl.updateGameState(gameState);
        this.question = new Scene(question.getValue());

        showQuestion();
        primaryStage.show();
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void showTimer() {
            primaryStage.setTitle("Countdown Timer");
            primaryStage.setScene(timer);
    }

    public void showQuestion() {
        primaryStage.setTitle("Question");
        primaryStage.setScene(question);
    }

//        choose.set(e -> chooseCtrl.Button1Pressed(e));


//        Scene scene = new Scene(ChooseAnswerCtrl.AnchorPane1, 640, 480);
//        primaryStage.setScene(scene);
//        primaryStage.show();

    public void handleGameState(GameState gameState) {
        //if any other screen is displayed there is something wrong.
        showQuestion();
        if (gameState.stage == GameState.Stage.QUESTION && gameState.question != null) {
            questionCtrl.clearAnswer();
            questionCtrl.setQuestion(gameState.question);
        }
        if (gameState.stage == GameState.Stage.INTERVAL) {
            questionCtrl.markAnswer(gameState.question.answer, gameState.playerAnswer);
        }
        questionCtrl.syncTimer(gameState.timerSyncLong, gameState.duration);
    }
}