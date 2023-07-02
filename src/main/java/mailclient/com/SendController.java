package mailclient.com;

import java.io.IOException;
import javafx.fxml.FXML;

public class SendController {
    private void switchToSend() throws IOException {
        App.setRoot("ServerSetting");

    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void switchToSetupInProgress() throws IOException {
        App.setRoot("SetupInProgress");
    }

}
