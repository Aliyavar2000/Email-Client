module mailclient.com {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.mail;
    requires org.json;

    opens mailclient.com.controllers to javafx.fxml;

    opens mailclient.com to javafx.base;

    exports mailclient.com;

    opens mailclient.com.models to javafx.base;

    opens mailclient.com.EmailAPI.receive to java.mail;

}
