Description of project
This is a quiz game. One game consists of 20 questions. There are multiple choice and open question. Each question has a 10 second maximum answer time. The faster a player submits their answer, the more points they get. There is a singleplayer mode and a multiplayer mode where players compete against each other. There is a leaderboard that can be accessed by pressing and holding TAB while in game. There are 3 power ups - Double points, Eliminate Wrong Answer, and Half time. Players can react to questions with emojis that are shown to all other players in the game. Multple games can be played at the same time, as there are lobbies. The questions are not hardcoded but imported. Every question consists of a question, an image, one correct answer and if multiple choice, multiple wrong answers. This format can be imported to the activity bank. Questions can be edited, added, and removed in the application itself. There is a world leaderboard where the top game scores are recorded together with the player that has achieved them.

System is implemented using a server-client architecture. One person hosts the game on their machine(the server) and everyone else connects. The communication is implemented with long polling. Frontend is made using JavaFX.

Showcase
Here you can see some images that showcase the general flow of the game

Start menu
image

Lobby
image

Question round, question is not yet answered
image

Question round, question is answered
image

Right after question round, answer is correct, points awarded
image

Another question, answered
image

Answer is incorrect, no points awarded
image

In game leaderboard(press TAB to view)
image

Aftergame lobby and final scoreboard
image

World Leaderboard
image

Admin Panel - no questions are imported
image

Admin Panel - a set of questions is imported
image

Group members
Anton Kalpakchiev | A.D.Kalpakchiev@student.tudelft.nl

Stefan Creasta | Creasta@student.tudelft.nl

Mert Bora İnevi | M.B.Inevi@student.tudelft.nl

Nguyen Duy Anh Quan | NguyenDuyAnhQuan@student.tudelft.nl

Alex Pacurar | A.V.Pacurar-1@student.tudelft.nl

Jort Boon | J.J.Boon@student.tudelft.nl

How to run it
The server has to be started before any clients join to it.

Adding the Activity Bank into the Project Directory
Go to the activity-bank repository releases page and download the "ZIP Archive with activities and images".

Extract its contents under a folder named activity-bank in
[project-root]\server\resources\images\.

In the end the path
[project-root]\server\resources\images\activity-bank\activities.json
should exist.

Note: You can use another folder name than activity-bank. This way, you can have multiple activity bank versions that you can import. For other names, you should use the different name for the following references to the activity-bank folder.

Starting the Server
Start The Server without Importing The Activities Automatically
From the command line
cd [project root]
.\gradlew build #if you haven't built yet
java -jar .\server\build\libs\server-0.0.1-SNAPSHOT.jar
# or
java -jar .\server\build\libs\server-0.0.1-SNAPSHOT.jar --server.port=8080
Within IntelliJ IDEA
Once the project is imported into IntelliJ IDEA, the server can be started from the automatically generated Spring run configuration.

Before any activities are imported and the question database is populated, though, initiating new games won't work properly. You should either import activities from the Admin Panel available in the client or follow up with the next section.

Start The Server Importing The Activities Automatically
The server can also import an activity bank at startup if it is configured so.

From the command line
cd [project root]
.\gradlew build #if you haven't built yet
java -jar .\server\build\libs\server-0.0.1-SNAPSHOT.jar --activitiesSource=activity-bank
# or
java -jar .\server\build\libs\server-0.0.1-SNAPSHOT.jar --server.port=8080 --activitiesSource=activity-bank
Within IntelliJ IDEA
Assuming that the activity bank to be imported resides under the server directory so the path
[project-root]\server\resources\images\activity-bank\activities.json
exists, one needs to add --activitiesSource=activity-bank as a command line argument to the Spring run configuration or to a copy of it, as seen below.



Starting the Client
WARNING: If you want to play single player you also need to start the server on localhost on port 8080. See the Starting the Server section for instructions to do so. Since the player would like to play in single player offline, but our single player game works connected to the server, we made starting a local server a requirement for single player.

The client needs to be run from IntelliJ IDEA (preferably at least version 2021.2.3)

You will need a copy of the OpenJFX SDK for your operating system. Download one from https://gluonhq.com/products/javafx/ -- make sure it is an SDK -- and extract the archive contents under a folder that you can easily find (preferably not in this project's folder).

Add JavaFX as a module for the project under File>Project Structure...>Project Settings>Modules. Select the module quizzzz.client and on the right panel click "+">JARs or Directories... and select the lib path of your JavaFX copy.

Create an Application run configuration with

JDK version 16 or newer,
-cp quizzzz.client.main
main method class client.Main
and working directory the full path to the project root.
Modify Options>Add VM options and add the following to the box that appears.
Make sure that the correct modules are added to the JVM when the client is run. You can see the necessary VM options below. (Replace the module path with the lib path of your own copy of OpenJFX SDK)

--module-path C:\Users\user\Documents\javafx-sdk-17.0.2\lib 
--add-modules javafx.controls,javafx.fxml
Then start the client normally.

Note: If an Application run configuration for the client produces errors related to graphics, you can try using a Gradle run configuration instead. It is not much different how VM arguments are added into such a configuration.

Gradle project setting should be the full path to the [project root]\client like ~\Documents\oopp-project\client. You also need to add the same VM options as you would in an Application configuration.

How to contribute to it
In addition to setting up IntelliJ according to the instructions in the run section, you may also want to set up SceneBuilder for ease in editing the FXML files.

Download SceneBuilder from https://gluonhq.com/products/scene-builder/ and install.

After installing, find the location of its main executable. For example on Windows, you can search for "Scene Builder" in the start menu, right click and choose "open file location" and keep doing this to shortcuts until you arrive the actual EXE file.

Copy the executable file's path, go back to IntelliJ, go to File>Settings...>Language and Frameworks>JavaFX, and paste the path into Path to SceneBuilder.
