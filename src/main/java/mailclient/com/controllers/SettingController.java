package mailclient.com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import mailclient.com.App;
import mailclient.com.credentials.UserCredentials;
import mailclient.com.connectionData.ConnectionInfo;
import java.util.function.UnaryOperator;
import org.json.JSONObject;
import java.io.FileWriter;

public class SettingController implements Initializable {

    @FXML
    private TextField fname;

    @FXML
    private TextField email;

    @FXML
    private TextField user;

    @FXML
    private PasswordField pw;

    @FXML
    private TextField hsmtp;

    @FXML
    private TextField ppop3;

    @FXML
    private TextField hpop3;

    @FXML
    private TextField psmtp;

    @FXML
    private Button terminateApp;

    @FXML
    private BorderPane BorderSetting;

    Stage stage;

    @FXML
    void switchToHome(ActionEvent event) {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToSettings(ActionEvent event) {
        try {
            App.setRoot("SettingConfig");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToCompose(ActionEvent event) {
        try {
            App.setRoot("Send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void DlConfig(ActionEvent event) {

        String fnameValue = UserCredentials.getUserFullName();
        String emailValue = UserCredentials.getUserMail();
        String usernameValue = UserCredentials.getUsername();
        String passwordValue = UserCredentials.getPassword();
        String hostSmtpValue = ConnectionInfo.getHostSmtp();
        String portSmtpValue = Integer.toString(ConnectionInfo.getPortSmtp());
        String hostPop3Value = ConnectionInfo.getHostPop3();
        String portPop3Value = Integer.toString(ConnectionInfo.getPortPop3());

        createAndSaveJson(fnameValue, emailValue, usernameValue, passwordValue, hostSmtpValue, portSmtpValue,
                hostPop3Value, portPop3Value);
    }

    @FXML
    void syncMessages(ActionEvent event) {

    }

    @FXML
    void terminateApp(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Hambal! Are you sure you want to logout?");
        alert.setContentText("Press OK to continue");

        if (alert.showAndWait().get().equals(ButtonType.OK)) {

            stage = (Stage) BorderSetting.getScene().getWindow();
            stage.close();
        }

    }

    public void createAndSaveJson(String fnameValue, String emailValue, String usernameValue, String passwordValue,
            String hostSmtpValue, String portSmtpValue, String hostPop3Value, String portPop3Value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fname", fnameValue);
        jsonObject.put("email", emailValue);
        jsonObject.put("user", usernameValue);
        jsonObject.put("pw", passwordValue);
        jsonObject.put("hsmtp", hostSmtpValue);
        jsonObject.put("psmtp", portSmtpValue);
        jsonObject.put("hpop3", hostPop3Value);
        jsonObject.put("ppop3", portPop3Value);

        // Write the JSON object to a file
        try {
            FileWriter fileWriter = new FileWriter("ConfigData.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
            System.out.println("JSON file created successfully.");
        } catch (IOException e) {
            System.out.println("Error while creating JSON file: " + e.getMessage());
        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        String fnameValue = UserCredentials.getUserFullName();
        fname.setPromptText(fnameValue);
        String emailValue = UserCredentials.getUserMail();
        email.setPromptText(emailValue);
        String usernameValue = UserCredentials.getUsername();
        user.setPromptText(usernameValue);
        String passwordValue = UserCredentials.getPassword();
        pw.setPromptText(" ");
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.length() > pw.getText().length()) {
                change.setText("x".repeat(newText.length() - pw.getText().length())); // Mask with "x"
            }
            return change;
        };
        pw.setTextFormatter(new TextFormatter<>(new DefaultStringConverter(), "", filter));

        String hostSmtpValue = ConnectionInfo.getHostSmtp();
        hsmtp.setPromptText(hostSmtpValue);
        String portSmtpValue = Integer.toString(ConnectionInfo.getPortSmtp());
        psmtp.setPromptText(portSmtpValue);
        String hostPop3Value = ConnectionInfo.getHostPop3();
        hpop3.setPromptText(hostPop3Value);
        String portPop3Value = Integer.toString(ConnectionInfo.getPortPop3());
        ppop3.setPromptText(portPop3Value);

    }

}
