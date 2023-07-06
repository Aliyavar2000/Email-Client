package mailclient.com.controllers;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javafx.animation.PauseTransition;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import mailclient.com.App;
import mailclient.com.EmailAPI.Connection;
import mailclient.com.EmailAPI.receive.ReceiveMessages;
import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;
import mailclient.com.models.EmailModel;

public class HomepageController implements Initializable {

    private Integer initialCount = 0;

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

    private ObservableList<EmailModel> emailList;

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

    private List<ReceivedMessageModel> receivedMessages;

    @FXML
    private ProgressIndicator syncProgressIndicator;

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
        Connection connection = App.buildEmailConnection();
        ReceiveMessages messageReceiver = new ReceiveMessages(connection.getStore());
        Integer newMessageCount = messageReceiver.countMessages();
        Integer oldMessageCount = getMessagesCount();
        System.out.println("Old message count: " + oldMessageCount);
        System.out.println("New message count: " + newMessageCount);
        if (newMessageCount > oldMessageCount) {
            setMessageCount(newMessageCount);
            syncProgressIndicator.setProgress(0.2);
            List<ReceivedMessageModel> messages = App.fetchMessages(connection, newMessageCount - oldMessageCount);
            syncProgressIndicator.setProgress(0.5);
            String filepath = "src/main/resources/mailclient/com/savedMessages/savedMessages.json";
            if (messages != null) {
                messageReceiver.writeToJsonFile(messages, filepath);
                syncProgressIndicator.setProgress(0.8);
                initialCount = newMessageCount.intValue();
                initialize(null, null);
                subjectCol.setStyle("-fx-background-color: #2a9d8f");

            }
        }
        syncProgressIndicator.setProgress(1.0);
        // Reset the progress indicator after a short delay
        PauseTransition pause = new PauseTransition();
        pause.setOnFinished(e -> {
            syncProgressIndicator.setProgress(0.0);
        });
        pause.play();
    }

    @FXML
    void terminate(ActionEvent event) {

    }

    @FXML
    void switchToHome(ActionEvent event) {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // syncProgressIndicator.setProgress(0.0);
        emailList = FXCollections.observableArrayList();
        tableView.getStylesheets().add(getClass().getResource("/mailclient/com/styles/styles.css").toExternalForm());
        String filepath = "src/main/resources/mailclient/com/savedMessages/savedMessages.json";

        try (FileReader fileReader = new FileReader(filepath)) {
            JSONArray jsonArray = new JSONArray(new JSONTokener(fileReader));

            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    // Retrieve the sender
                    String sender = jsonObject.getString("Sender");

                    // Retrieve the receivers
                    String[] receivers = new String[1];
                    try {
                        String receiversString = jsonObject.getString("Receivers");
                        receivers = receiversString.split(",");
                    } catch (Exception e) {
                        receivers[0] = "Receivers unknown";
                    }

                    // Retrieve the subject
                    String subject;
                    try {
                        subject = jsonObject.getString("Subject");
                    } catch (JSONException e) {
                        // Handle the exception when "Subject" field is not found
                        subject = "No Subject";
                    }

                    // Retrieve the content
                    String content;
                    try {
                        content = jsonObject.getString("Content");
                    } catch (JSONException e) {
                        // Handle the exception when "Content" field is not found
                        content = "";
                    }

                    // Retrieve the received date
                    String receivedDate;
                    try {
                        receivedDate = jsonObject.getString("ReceivedDate");
                    } catch (JSONException e) {
                        // Handle the exception when "ReceivedDate" field is not found
                        receivedDate = "";
                    }

                    // Retrieve the sent date
                    String sentDate;
                    try {
                        sentDate = jsonObject.getString("SentDate");
                    } catch (JSONException e) {
                        // Handle the exception when "SentDate" field is not found
                        sentDate = "";
                    }

                    emailList.add(0, new EmailModel(sender, receivers, subject, content, sentDate, receivedDate));
                }

                tableView.setItems(emailList);
                setMessageCount(emailList.size());

            } catch (JSONException e) {
                // Handle the exception when retrieving fields from JSONObject
                System.out.println("Error retrieving fields from JSONObject: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                int rowIndex = tableView.getSelectionModel().getSelectedIndex();

                if (rowIndex >= 0) {
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

        tableView.setItems(emailList);
    }

    public void setReceivedMessages(List<ReceivedMessageModel> messages) {
        emailList = FXCollections.observableArrayList();
        for (ReceivedMessageModel message : messages) {
            emailList.add(new EmailModel(message.getFrom(), message.getTo(), message.getSubject(), message.getContent(),
                    message.getSentDate(), message.getReceivedDate()));
        }
    }

    private int getMessagesCount() {
        // initialCount = emailList.size();
        return initialCount;
    }

    private void setMessageCount(Integer num) {
        if (num > initialCount) {
            initialCount = num.intValue();
        }
    }
}
