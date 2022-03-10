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

    private ChooseAnswerCtrl chooseCtrl;
    private Scene choose;

    private ChoosePowerUpsCtrl choosePowerUpCtrl;
    private Scene choosePower;

    public void initialize(Stage primaryStage,
                           Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<ChooseAnswerCtrl, Parent> chooseAnswerPair,
                           Pair<CountdownTimer,Parent> timer,
                           Pair<ChoosePowerUpsCtrl,Parent> choosePower) {

        this.primaryStage = primaryStage;

        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.timerCtrl  = timer.getKey();
        this.timer = new Scene(timer.getValue());

        this.chooseCtrl = chooseAnswerPair.getKey();
        this.choose = new Scene(chooseAnswerPair.getValue());

        this.choosePowerUpCtrl = choosePower.getKey();
        this.choosePower = new Scene(choosePower.getValue());

        //showOverview();
        showPowerUps();
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

    public void showChooseAnswer() {
        primaryStage.setTitle("Choose Answer");
        primaryStage.setScene(choose);
//        choose.set(e -> chooseCtrl.Button1Pressed(e));


//        Scene scene = new Scene(ChooseAnswerCtrl.AnchorPane1, 640, 480);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }
    public void showPowerUps() {
        primaryStage.setTitle("Power Ups");
        primaryStage.setScene(choosePower);
    }
}