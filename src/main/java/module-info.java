module com.mycompany.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;
    requires derbyclient;

    opens com.mycompany.tictactoeserver to javafx.fxml;
    exports com.mycompany.tictactoeserver;
    exports com.mycompany.tictactoeserver.presentation;
    opens com.mycompany.tictactoeserver.presentation to javafx.fxml;
}
