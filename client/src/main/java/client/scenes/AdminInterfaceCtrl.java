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
            error(e.getMessage());
        }
    }

    public void tableClick() {
        if (selectedQuestion == null || !selectedQuestion.equals(activitiesTable.getSelectionModel().getSelectedItem())) {
            selectedQuestion = activitiesTable.getSelectionModel().getSelectedItem();
            setQuestionInputs(selectedQuestion);
        } else {
            activitiesTable.getSelectionModel().clearSelection();
            selectedQuestion = null;
            setQuestionInputs(null);
        }
    }

    public void search(ActionEvent actionEvent) {
        try {
            var questions = questionCommunication.searchBy(searchField.getText(), urlField.getValue());
            activitiesTable.setItems(FXCollections.observableList(questions));
            selectedQuestion = null;

            saveServerUrl(urlField.getValue());
        }
        catch (ConnectException e) {
            error(e.getMessage());
        }
    }

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
                error(e.getMessage());
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
                error(e.getMessage());
                return;
            }
            activitiesTable.getItems().set(activitiesTable.getItems().indexOf(selectedQuestion), q);
            activitiesTable.getSelectionModel().select(selectedQuestion);
        }
    }

    public void delete(ActionEvent actionEvent) {
        try {
            questionCommunication.deleteQuestion(selectedQuestion.id, urlField.getValue());
            activitiesTable.getSelectionModel().clearSelection();
            activitiesTable.getItems().remove(selectedQuestion);
            selectedQuestion = null;
            setQuestionInputs(null);

            saveServerUrl(urlField.getValue());
        } catch (ConnectException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }

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

    public void markError(TextField field) {
        field.getStyleClass().add("error");
    }

    public void clearError(TextField field) {
        field.getStyleClass().remove("error");
    }

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

    public void error(String s) {
        errorAlert.setTitle("Error");
        errorAlert.setContentText(s);
        errorAlert.show();
    }

    public void info(String s, String title) {
        infoAlert.setTitle(title);
        infoAlert.setContentText(s);
        infoAlert.show();
    }

    public void registerServerUrlList(List<String> list) {
        urlField.setItems(FXCollections.observableList(list));
    }

    public void saveServerUrl(String s) {
        mainCtrl.saveServerUrl(s);
    }
}
