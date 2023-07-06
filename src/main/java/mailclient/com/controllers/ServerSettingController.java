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

    // @FXML
    // void switchToLogin(ActionEvent event) {

    // }

    // @FXML
    // void switchToSetupInProgress(ActionEvent event) {

    // }

    @FXML
    private void switchToSetupInProgress() throws IOException {
        String outgoingPort = outgoingPortField.getText();
        int outgoingPortInt = Integer.parseInt(outgoingPort);
        String outgoingHostname = outgoingHostnameField.getText();
        String incomingPort = incomingPortField.getText();
        int incomingPortInt = Integer.parseInt(incomingPort);
        String incomingHostname = incomingHostnameField.getText();
        // System.out.println("outgoingHostname: " + outgoingHostname);
        // System.out.println("outgoingPort: " + outgoingPortInt);
        // System.out.println("incomingHostname: " + incomingHostname);
        // System.out.println("incomingPort: " + incomingPortInt);
        ConnectionInfo.initialize(incomingHostname, incomingPortInt,
                outgoingHostname, outgoingPortInt);
        App.setRoot("Homepage");
        // App.setRoot("SetupInProgress");
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }
}