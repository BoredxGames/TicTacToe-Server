module com.mycompany.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;
    requires derbyclient;


    opens com.mycompany.tictactoeserver to javafx.fxml;
    opens com.mycompany.tictactoeserver.presentation to javafx.fxml;

    exports com.mycompany.tictactoeserver;
    exports com.mycompany.tictactoeserver.presentation;
    exports com.mycompany.tictactoeserver.domain.server;
    exports com.mycompany.tictactoeserver.domain.utils.exception;
    exports com.mycompany.tictactoeserver.domain.utils.callbacks;
    exports com.mycompany.tictactoeserver.datasource.model;

}
