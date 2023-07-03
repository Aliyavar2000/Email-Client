module mailclient.com {
    requires javafx.controls;
    requires javafx.fxml;

    opens mailclient.com to javafx.fxml;

    exports mailclient.com;

    opens mailclient.com.models to javafx.base;

}
