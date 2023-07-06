package mailclient.com;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mailclient.com.EmailAPI.Connection;
import mailclient.com.EmailAPI.PingServer;
import mailclient.com.EmailAPI.receive.ReceiveMessages;
import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;
import mailclient.com.connectionData.ConnectionInfo;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML("Login"));
        // Screen screen = Screen.getPrimary();
        // Rectangle2D bounds = screen.getVisualBounds();
        stage.setWidth(1080);
        stage.setHeight(620);
        stage.setResizable(false);
        scene.getStylesheets()
                .add(getClass().getResource("/mailclient/com/styles/styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // public static <T> T injectDataToController(String fxml) {
    // try {
    // FXMLLoader fxmlLoader = new
    // FXMLLoader(App.class.getResource("/mailclient/com/scenes/" + fxml +
    // ".fxml"));
    // Parent root = fxmlLoader.load();
    // T controller = fxmlLoader.getController();
    // return controller;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(
                "/mailclient/com/scenes/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Connection buildEmailConnection() {
        try {
            PingServer connectionStatusPop3 = new PingServer(ConnectionInfo.getHostPop3(),
                    ConnectionInfo.getPortPop3());
            Connection connectionPop3 = new Connection();
            if (checkTheSerever(connectionStatusPop3)) {
                connectionPop3.buildEmailConnection();
            }
            return connectionPop3;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    public static List<ReceivedMessageModel> fetchMessages(Connection connectionPop3) {
        ReceiveMessages messageReceiver = new ReceiveMessages(connectionPop3.getStore());
        return messageReceiver.getMessages();
    }

    public static List<ReceivedMessageModel> fetchMessages(Connection connectionPop3, int messageCount) {
        ReceiveMessages messageReceiver = new ReceiveMessages(connectionPop3.getStore());
        return messageReceiver.getMessages(messageCount);
    }

    private static boolean checkTheSerever(PingServer connectionStatusPop3) {
        try {
            if (connectionStatusPop3.pingServer()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        launch();
    }

}
