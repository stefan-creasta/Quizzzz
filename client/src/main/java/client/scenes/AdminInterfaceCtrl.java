package client.scenes;

import client.Communication.ImporterCommunication;
import client.Communication.QuestionCommunication;
import com.google.inject.Inject;
import commons.Question;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.net.ConnectException;
import java.util.List;
import java.util.Objects;

public class AdminInterfaceCtrl {
    @FXML private ComboBox<String> urlField;
    @FXML private TableView<Question> activitiesTable;
    @FXML private TableColumn<Question, String> activityColumn;
    @FXML private TableColumn<Question, String> wattsColumn;
    @FXML private TableColumn<Question, String> imagePathColumn;
    @FXML private Button saveButton;
    @FXML private TextField searchField;
    @FXML private TextField activityField;
    @FXML private TextField wattsField;
    @FXML private TextField imagePathField;
    @FXML private Button deleteButton;
    @FXML private TextField pathField;

    private Alert errorAlert;
    private Alert infoAlert;

    private MainCtrl mainCtrl;

    private ImporterCommunication importerCommunication;
    private final QuestionCommunication questionCommunication;

    private Question selectedQuestion;

    @Inject
    public AdminInterfaceCtrl(ImporterCommunication importerCommunication, QuestionCommunication questionCommunication, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.importerCommunication = importerCommunication;
        this.questionCommunication = questionCommunication;
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.infoAlert = new Alert(Alert.AlertType.INFORMATION);
    }

    public void initialize() {
        activityColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().question));
        wattsColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().answer));
        imagePathColumn.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().questionImage));

        AnchorPane.setLeftAnchor(activitiesTable, activitiesTable.getLayoutX());
        AnchorPane.setRightAnchor(activitiesTable, activitiesTable.getLayoutX());

        activitiesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        setQuestionInputs(null);
    }


    /**
     * The event handler for the server URL field.
     * Clears the activities table and clears any input.
     */
    public void serverUrlChange(ActionEvent actionEvent) {
        if (selectedQuestion != null) {
            setQuestionInputs(null);
        }
        selectedQuestion = null;
        activitiesTable.setItems(FXCollections.observableList(List.of()));
        pathField.setText("");
    }

    /**
     * The event handler for the "Back to Menu" button.
     * Tells MainCtrl to show the splash screen.
     * @param actionEvent
     */
    public void back(ActionEvent actionEvent) {
        mainCtrl.showSplashScreen();
    }

    public void importQuestions(ActionEvent actionEvent) {
        String message = null;
        try {
            message = importerCommunication.importQuestions(pathField.getText(), urlField.getValue());

            info(message, "Result");

            saveServerUrl(urlField.getValue());
        } catch (ConnectException e) {
            e.printStackTrace();
            error("Error connecting \"" + urlField.getValue() + "\"");
        }
    }

    /**
     * The event handler for when the activities table is clicked. It either selects or deselects a question.
     */
    public void tableClick() {
        //if no item was selected before or the user has just selected a different item.
        if (selectedQuestion == null || !selectedQuestion.equals(activitiesTable.getSelectionModel().getSelectedItem())) {
            selectedQuestion = activitiesTable.getSelectionModel().getSelectedItem();
            setQuestionInputs(selectedQuestion);
        //if the user is trying to select the same item, deselect instead.
        } else {
            activitiesTable.getSelectionModel().clearSelection();
            selectedQuestion = null;
            setQuestionInputs(null);
        }
    }

    /**
     * The event handler for the "Search" button.
     * Fetches the search results for the inputted search keywords and shows the results on the activities table.
     * @param actionEvent
     */
    public void search(ActionEvent actionEvent) {
        try {
            var questions = questionCommunication.searchBy(searchField.getText(), urlField.getValue());
            activitiesTable.setItems(FXCollections.observableList(questions));
            selectedQuestion = null;

            saveServerUrl(urlField.getValue());
        }
        catch (ConnectException e) {
            e.printStackTrace();
            error("Error connecting \"" + urlField.getValue() + "\"");
        }
    }

    /**
     * The event handler for the "Add/Update Activity" button.
     * Saves the inputted activity if nothing was selected in the table, or updates the selected item otherwise.
     * It also checks for any invalid inputs through the {@code validateInputs} method.
     * @param actionEvent
     */
    public void save(ActionEvent actionEvent) {
        if (!validateQuestionInputs()) return;

        Question q = new Question(
                0L,
                activityField.getText(),
                wattsField.getText(),
                null,
                null,
                "1"
        );
        q.questionImage = imagePathField.getText();
        if (selectedQuestion == null) {
            try {
                questionCommunication.postQuestion(q, urlField.getValue());
                info("The activity was added successfully.", "Success");

                saveServerUrl(urlField.getValue());
            } catch (ConnectException e) {
                e.printStackTrace();
                error("Error connecting \"" + urlField.getValue() + "\"");
            }
        } else {
            q.id = selectedQuestion.id;
            q.question = Objects.equals(selectedQuestion.question, activityField.getText()) ? null : activityField.getText();
            q.answer = Objects.equals(selectedQuestion.answer, wattsField.getText()) ? null : wattsField.getText();
            q.wrongAnswer1 = null;
            q.wrongAnswer2 = null;
            q.questionImage = Objects.equals(selectedQuestion.questionImage, imagePathField.getText()) ? null : imagePathField.getText();
            try {
                q = questionCommunication.patchQuestion(q, urlField.getValue());
                info("The activity was updated successfully.", "Success");

                saveServerUrl(urlField.getValue());
            } catch (ConnectException e) {
                e.printStackTrace();
                error("Error connecting \"" + urlField.getValue() + "\"");
                return;
            }
            activitiesTable.getItems().set(activitiesTable.getItems().indexOf(selectedQuestion), q);
            activitiesTable.getSelectionModel().select(selectedQuestion);
        }
    }

    /**
     * The event handler for the "Delete" button.
     * Deletes the selected activity.
     * @param actionEvent
     */
    public void delete(ActionEvent actionEvent) {
        if (selectedQuestion == null) return;
        try {
            questionCommunication.deleteQuestion(selectedQuestion.id, urlField.getValue());
            activitiesTable.getSelectionModel().clearSelection();
            activitiesTable.getItems().remove(selectedQuestion);
            selectedQuestion = null;
            setQuestionInputs(null);

            saveServerUrl(urlField.getValue());
        } catch (ConnectException e) {
            e.printStackTrace();
            error("Error connecting \"" + urlField.getValue() + "\"");
        }
    }

    /**
     * Validates the activity input fields.
     * Adds the style class "error" to any field that has an error.
     * Removes the class from a field if there aren't any errors in the field.
     * @return true iff all inputs are valid.
     */
    public boolean validateQuestionInputs() {
        boolean validation = true;
        try {
            Double.parseDouble(wattsField.getText());
            clearError(wattsField);
        } catch (NumberFormatException e) {
            markError(wattsField);
            validation = false;
        }
        return validation;
    }

    /**
     * Adds the "error" style class to the provided TextField if it doesn't have one.
     * @param field
     */
    public void markError(TextField field) {
        field.getStyleClass().add("error");
    }

    /**
     * Removes the "error" style class from the provided TextField if it has one.
     * @param field
     */
    public void clearError(TextField field) {
        field.getStyleClass().remove("error");
    }

    /**
     * Sets the activity input fields according to the provided Question.
     * If Question is null, then the fields are emptied except the Image Path field,
     * the text in which can be potentially used for adding a new question.
     * @param q
     */
    public void setQuestionInputs(Question q) {
        if (q == null) {
            deleteButton.setVisible(false);
            saveButton.setText("Add Activity");

            activityField.setText("");
            wattsField.setText("");
            //imagePathField.setText("");
        } else {
            deleteButton.setVisible(true);
            saveButton.setText("Update Activity");

            activityField.setText(q.question);
            wattsField.setText(q.answer);
            imagePathField.setText(q.questionImage);
        }
    }

    /**
     * Shows an error dialog with the provided message. It doesn't block the execution of the main window.
     * @param s
     */
    public void error(String s) {
        errorAlert.setTitle("Error");
        errorAlert.setContentText(s);
        errorAlert.show();
    }

    /**
     * Shows an info dialog with the provided message. It doesn't block the execution of the main window.
     * @param s the body text of the dialog box
     * @param title the title of the dialog box
     */
    public void info(String s, String title) {
        infoAlert.setTitle(title);
        infoAlert.setContentText(s);
        infoAlert.show();
    }

    /**
     * Makes the server URL field observe the values in the MainCtrl serverUrls list.
     * It is called from mainCtrl when the server list file was read and the serverUrls list is ready.
     * @param list
     */
    public void registerServerUrlList(List<String> list) {
        urlField.setItems(FXCollections.observableList(list));
    }

    /**
     * Saves the provided URL to the MainCtrl serverUrls list.
     * @param s
     */
    public void saveServerUrl(String s) {
        mainCtrl.saveServerUrl(s);
    }
}
