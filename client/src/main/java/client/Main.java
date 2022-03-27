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


import client.Communication.GameCommunication;
import client.Communication.ServerListener;
import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;


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
        var timer = FXML.load(CountdownTimer.class,"client","scenes","Timer.fxml");

        var question = FXML.load(QuestionCtrl.class, "client", "scenes", "Question.fxml");
        var lobby = FXML.load(LobbyCtrl.class, "client", "scenes", "Lobby.fxml");
        var player = FXML.load(AddPlayerCtrl.class, "client", "scenes", "AddPlayer.fxml");

        var serverListener = INJECTOR.getInstance(ServerListener.class);
        var gameCommunication = INJECTOR.getInstance(GameCommunication.class);
        var splashScreen = FXML.load(SplashScreenCtrl.class, "client", "scenes", "SplashScreen.fxml");

        long playerId = 1;

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, overview, add,  question, timer, lobby, player, gameCommunication, serverListener, splashScreen);

        serverListener.initialize(playerId, mainCtrl);
    }
}