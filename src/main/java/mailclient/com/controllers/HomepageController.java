package mailclient.com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
// import javafx.util.Callback;
import mailclient.com.App;
import mailclient.com.models.EmailModel;

public class HomepageController implements Initializable {
    @FXML
    private TableView<EmailModel> tableView;

    @FXML
    private Button homepageButton;

    @FXML
    private Button composeButton;

    @FXML
    private TableColumn<EmailModel, String> fromCol;

    @FXML
    private Button quitButton;

    @FXML
    private Button receiveMessagesButton;

    @FXML
    private TableColumn<EmailModel, String> receivedCol;

    @FXML
    private TableColumn<EmailModel, RadioButton> selectCol;

    @FXML
    private TableColumn<EmailModel, String> sentCol;

    @FXML
    private Button settingsButton;

    @FXML
    private TableColumn<EmailModel, String> subjectCol;

    @FXML
    private TableColumn<EmailModel, String> toCol;

    private ObservableList<EmailModel> emailList = FXCollections.observableArrayList(
            new EmailModel("from", "to", "subject", "content", "dateSent", "dateReceived"),
            new EmailModel("from", "to", "subject", "content", "dateSent", "dateReceived"),
            new EmailModel("from", "to", "subject", "content", "dateSent", "dateReceived"),
            new EmailModel("from", "to", "subject", "content", "dateSent", "dateReceived"),
            new EmailModel("from", "to", "subject", "content", "dateSent", "dateReceived"));

    @FXML
    private GridPane selectedMessageMeta;

    @FXML
    private Label messageTo;

    @FXML
    private Label messageFrom;

    @FXML
    private Label messageSubject;

    @FXML
    private TextArea messageText;

    @FXML
    private AnchorPane AnchorHome;

    @FXML
    private Button terminateApp;

    Stage stage;

    @FXML
    void switchToCompose(ActionEvent event) {
        try {
            App.setRoot("Send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToSettings(ActionEvent event) {
        try {
            App.setRoot("SettingConfig");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void syncMessages(ActionEvent event) {

    }

    @FXML
    public void terminateApp(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Hambal! Are you sure you want to logout?");
        alert.setContentText("Press OK to continue");

        if (alert.showAndWait().get().equals(ButtonType.OK)) {

            stage = (Stage) AnchorHome.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    void switchToHome(ActionEvent event) throws IOException {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.getStylesheets().add(getClass().getResource("/mailclient/com/styles/styles.css").toExternalForm());
        for (int i = 0; i < 25; i++) {
            emailList.add(new EmailModel("from", "to", "subject", "content" + i, "dateSent", "dateReceived"));
        }
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                int rowIndex = tableView.getSelectionModel().getSelectedIndex();

                if (rowIndex >= 0) {
                    // messageText.clear();
                    // System.out.println("Clicked row index: " + rowIndex);
                    String content = tableView.getItems().get(rowIndex).getContent();
                    messageText.setText(content);
                    String from = tableView.getItems().get(rowIndex).getFrom();
                    messageFrom.setText(from);
                    String to = tableView.getItems().get(rowIndex).getTo();
                    messageTo.setText(to);
                    String subject = tableView.getItems().get(rowIndex).getSubject();
                    messageSubject.setText(subject);
                }
            }
        });
        fromCol.setCellValueFactory(new PropertyValueFactory<EmailModel, String>("from"));
        toCol.setCellValueFactory(new PropertyValueFactory<EmailModel, String>("to"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailModel, String>("subject"));
        sentCol.setCellValueFactory(new PropertyValueFactory<EmailModel, String>("dateSent"));
        receivedCol.setCellValueFactory(new PropertyValueFactory<EmailModel, String>("dateReceived"));
        // selectCol.setCellValueFactory(cellData ->

        // {
        // EmailModel emailModel = cellData.getValue();
        // RadioButton radioButton = new RadioButton();
        // BooleanProperty selectedProperty = new SimpleBooleanProperty();

        // radioButton.selectedProperty().bindBidirectional(selectedProperty);
        // selectedProperty.addListener((observable, oldValue, newValue) ->
        // emailModel.setSelected(newValue));

        // return new ObservableValue<RadioButton>() {
        // @Override
        // public void addListener(InvalidationListener listener) {
        // selectedProperty.addListener((observable) -> listener.invalidated(this));
        // }

        // @Override
        // public void removeListener(InvalidationListener listener) {
        // selectedProperty.removeListener((observable) -> listener.invalidated(this));
        // }

        // @Override
        // public void addListener(ChangeListener<? super RadioButton> listener) {
        // selectedProperty.addListener(
        // (observable, oldValue, newValue) -> listener.changed(this, radioButton,
        // radioButton));
        // }

        // @Override
        // public void removeListener(ChangeListener<? super RadioButton> listener) {
        // selectedProperty.removeListener(
        // (observable, oldValue, newValue) -> listener.changed(this, radioButton,
        // radioButton));
        // }

        // @Override
        // public RadioButton getValue() {
        // return radioButton;
        // }
        // };
        // });
        tableView.setItems(emailList);
    }
}
