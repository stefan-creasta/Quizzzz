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
package client;




import static com.google.inject.Guice.createInjector;
import java.io.IOException;
import java.net.URISyntaxException;

import client.Communication.GameCommunication;
import client.Communication.ServerListener;
import client.scenes.ChooseAnswerCtrl;
import com.google.inject.Injector;
import client.scenes.*;
import commons.GameState;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        var overview = FXML.load(QuoteOverviewCtrl.class, "client", "scenes", "QuoteOverview.fxml");
        var add = FXML.load(AddQuoteCtrl.class, "client", "scenes", "AddQuote.fxml");
        var chooseAnswer = FXML.load(ChooseAnswerCtrl.class, "client", "scenes", "ChooseAnswer.fxml");
        var timer = FXML.load(CountdownTimer.class,"client","scenes","Timer.fxml");
        var question = FXML.load(QuestionCtrl.class, "client", "scenes", "Question.fxml");
        var choosePower = FXML.load(ChoosePowerUpsCtrl.class,"client","scenes","ChoosePowerUps.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add, chooseAnswer,  question, choosePower, timer);

        var serverListener = INJECTOR.getInstance(ServerListener.class);
        var gameCommunication = INJECTOR.getInstance(GameCommunication.class);
        Pair<Long, GameState> gameInfo = hardcodedThingsForDemo(gameCommunication);
        long gameId = gameInfo.getKey();
        GameState state = gameInfo.getValue();
        long playerId = state.getPlayer().id;
        serverListener.initialize(playerId, mainCtrl);
        gameCommunication.initiateGame(gameId);
    }


    public Pair<Long, GameState> hardcodedThingsForDemo(GameCommunication gameComm) {
        try {
            long gameId = gameComm.createGame();
            Thread.sleep(100);
            GameState state = gameComm.joinGame(gameId, "group53");
            Thread.sleep(100);
            return new Pair<>(gameId, state);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}