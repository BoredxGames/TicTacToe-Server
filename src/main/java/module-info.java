module com.mycompany.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;
    requires derbyclient;
    requires com.google.gson;

    opens com.mycompany.tictactoeserver to javafx.fxml;
    opens com.mycompany.tictactoeserver.presentation to javafx.fxml;
    opens com.mycompany.tictactoeserver.domain.services.communication to com.google.gson;
    opens com.mycompany.tictactoeserver.domain.entity to com.google.gson;

    exports com.mycompany.tictactoeserver;
    exports com.mycompany.tictactoeserver.presentation;
    exports com.mycompany.tictactoeserver.domain.server;
    exports com.mycompany.tictactoeserver.domain.utils.exception;
    exports com.mycompany.tictactoeserver.domain.utils.callbacks;
    exports com.mycompany.tictactoeserver.datasource.model;

}
