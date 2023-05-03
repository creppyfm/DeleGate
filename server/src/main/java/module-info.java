module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.creppyfm.server to javafx.fxml;
    exports com.creppyfm.server;
}