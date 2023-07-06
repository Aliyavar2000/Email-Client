package mailclient.com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mailclient.com.App;
import mailclient.com.EmailAPI.Connection;
import mailclient.com.connectionData.MessageData;
import mailclient.com.credentials.UserCredentials;

public class SendController implements Initializable {

    @FXML
    private Button CancelSending;

    @FXML
    private TextField CcBox;

    @FXML
    private Button ContinueToSendSuccessfully;

    @FXML
    private TextField FromBox;

    @FXML
    private Button HomeButton;

    @FXML
    private TextArea MessageBox;

    @FXML
    private TextField SubjectBox;

    @FXML
    private TextField ToBox;

    @FXML
    private Button composeButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button syncButton;

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
            App.setRoot("Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void syncMessages(ActionEvent event) {

    }

    @FXML
    void terminate(ActionEvent event) {

    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FromBox.setPromptText(UserCredentials.getUserMail());
    }

    public void send(ActionEvent event) throws IOException {
        try {

            String to = ToBox.getText();
            // Error handling
            if (to.isEmpty() || to.contains("!") || to.contains(",") || to.contains(";") || to.contains(":")
                    || to.contains(" ") || !to.contains("@") || !to.contains(".")) {
                ToBox.clear();
                ToBox.setPromptText("Enter a valid recipient!");
                return;
            }

            String cc = CcBox.getText();
            // Error handling
            if (cc.isEmpty() || (cc.contains("!") || cc.contains(",") || cc.contains(";") || cc.contains(":")
                    || cc.contains(" ") || !cc.contains("@"))) {
                CcBox.clear();
                CcBox.setPromptText("Enter a valid recipient!");
                return;
            }

            String subject = SubjectBox.getText();
            // Error handling
            if (subject.isEmpty()) {
                SubjectBox.clear();
                SubjectBox.setPromptText("Enter a subject!");
                return;
            }

            String message = MessageBox.getText();
            // Error handling
            if (message.isEmpty()) {
                MessageBox.clear();
                MessageBox.setPromptText("Enter a message!");
                return;
            }
            Connection establishConnection = new Connection();
            MessageData messageData = new MessageData(to, cc, subject, message);

            System.out.println("Sent!");
            // switchToSetupInProgress();

        } catch (Exception e) {
            System.out.println("Failed to send!");
        }
    }

    public String getFrom() {
        // LoginController loginController = new LoginController();
        // String from = loginController.getEmail();
        return FromBox.getText();
    }

    public String getTo() {
        return ToBox.getText();
    }

    public String getCc() {
        return CcBox.getText();
    }

    public String getSubject() {
        return SubjectBox.getText();
    }

    public String getMessage() {
        return MessageBox.getText();
    }
}
