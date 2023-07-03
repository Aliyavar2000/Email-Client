package mailclient.com;

import java.io.IOException;
import javafx.fxml.FXML;

public class ServerSettingController {

    @FXML
    private void switchToSetupInProgress() throws IOException {
        App.setRoot("Homepage");
        // App.setRoot("SetupInProgress");
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void handleKeyPress() {
        // outgoingHostnameField.promptTextProperty().setValue("Enter your hostname");
    }
}