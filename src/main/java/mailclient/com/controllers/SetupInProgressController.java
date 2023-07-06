package mailclient.com.controllers;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import mailclient.com.App;
import mailclient.com.EmailAPI.Connection;
import mailclient.com.EmailAPI.PingServer;
import mailclient.com.EmailAPI.receive.ReceiveMessages;
import mailclient.com.EmailAPI.receive.model.Pop3Model;
import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;
import mailclient.com.connectionData.ConnectionInfo;
import mailclient.com.credentials.UserCredentials;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class SetupInProgressController implements Initializable {
    Pop3Model pop3Model;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label errorMessage;

    private boolean homepageSwitched = false;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("Connecting to server...");
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Connection connection = App.buildEmailConnection();
                    updateProgress(0.2, 1.0);
                    Platform.runLater(() -> errorMessage.setText("Fetching messages..."));
                    List<ReceivedMessageModel> messages = App.fetchMessages(connection);
                    updateProgress(0.4, 1.0);
                    String filepath = "src/main/resources/mailclient/com/savedMessages/savedMessages.json";
                    updateProgress(0.6, 1.0);
                    if (messages != null) {
                        // Save received messages to json file
                        ReceiveMessages messageReceiver = new ReceiveMessages(connection.getStore());
                        Platform.runLater(() -> errorMessage.setText("Saving messages..."));
                        updateProgress(0.8, 1.0);
                        Platform.runLater(() -> errorMessage.setText("Finishing the setup..."));
                        messageReceiver.writeToJsonFile(messages, filepath);
                    }
                    updateProgress(1.0, 1.0);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateProgress(0.0, 1.0);
                    Platform.runLater(() -> errorMessage.setText("Something went wrong. Please try again."));
                    throw new Exception();
                }

                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(event -> {
            switchToHomepage();
        });

        task.setOnFailed(event -> {
            errorMessage.setText("Something went wrong. Please try again.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                App.setRoot("ServerSetting");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void switchToHomepage() {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // private void startProgressBarAnimation() {
    // Timeline timeline = new Timeline(
    // new KeyFrame(Duration.seconds(0.5), event -> {
    // if (!homepageSwitched && progressBar.getProgress() >= 1.0) {
    // // Switch to homepage
    // try {
    // switchToHomepage();
    // homepageSwitched = true; // Set the flag to indicate switch occurred
    // } catch (IOException e) {
    // e.printStackTrace();
    // // Handle the exception appropriately
    // }
    // }
    // }));
    // timeline.setCycleCount(Timeline.INDEFINITE);
    // timeline.play();

    // // Simulate the progress being filled
    // simulateProgress();
    // }

    private void simulateProgress() {
        Timeline simulateTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    double currentProgress = progressBar.getProgress();
                    if (currentProgress < 1.0) {
                        progressBar.setProgress(currentProgress + 0.1);
                    }
                }));
        simulateTimeline.setCycleCount(10);
        simulateTimeline.play();
    }

    private void saveReceivedMessagesToJson(List<ReceivedMessageModel> messages) {
        JSONArray jsonArray = new JSONArray();

        for (ReceivedMessageModel message : messages) {
            JSONObject jsonMessage = new JSONObject();
            // jsonMessage.put("id", message.getId());
            jsonMessage.put("Sender", message.getFrom());
            jsonMessage.put("Receivers", message.getTo());
            jsonMessage.put("Content", message.getContent());
            jsonMessage.put("ReceivedDate", message.getReceivedDate());
            jsonMessage.put("SentDate", message.getSentDate());

            jsonArray.put(jsonMessage);
        }

        try (FileWriter file = new FileWriter("savedMessages.json")) {
            file.write(jsonArray.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
