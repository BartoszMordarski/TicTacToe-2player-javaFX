module org.example.ttt {
    requires javafx.controls;
    requires javafx.fxml;
    requires Java.WebSocket;
    requires java.sql;


    exports org.example.ttt.client;
    opens org.example.ttt.client to javafx.fxml;
}