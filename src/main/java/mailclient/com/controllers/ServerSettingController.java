package mailclient.com.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mailclient.com.App;
import mailclient.com.connectionData.ConnectionInfo;

public class ServerSettingController {

    @FXML
    private Button cancelSetting;

    @FXML
    private Button continueToSetupProgress;

    @FXML
    private TextField incomingHostnameField;

    @FXML
    private TextField incomingPortField;

    @FXML
    private TextField outgoingHostnameField;

    @FXML
    private TextField outgoingPortField;

    @FXML
    void switchToLogin(ActionEvent event) {

    }

    // @FXML
    // void switchToSetupInProgress(ActionEvent event) {

    // }

    @FXML
    private void switchToSetupInProgress() throws IOException {
        String outgoingPort = outgoingPortField.getText().trim();
        String outgoingHostname = outgoingHostnameField.getText().trim();
        String incomingPort = incomingPortField.getText().trim();
        String incomingHostname = incomingHostnameField.getText().trim();
        ConnectionInfo.initialize(incomingHostname, Integer.parseInt(incomingPort), outgoingHostname,
                Integer.parseInt(outgoingPort));
        App.setRoot("SetupInProgress");
        // App.setRoot("SetupInProgress");
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }
}