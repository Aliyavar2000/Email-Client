package mailclient.com;

import java.io.IOException;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private void switchToServerSetting() throws IOException {
        App.setRoot("ServerSetting");
    }

    private void terminateApp() throws IOException {
        // App.setRoot("SetupInProgress");
    }
}
