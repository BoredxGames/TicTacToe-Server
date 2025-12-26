module com.mycompany.tictactoeserver {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.tictactoeserver to javafx.fxml;
    exports com.mycompany.tictactoeserver;
    exports com.mycompany.tictactoeserver.presentation;
    opens com.mycompany.tictactoeserver.presentation to javafx.fxml;
}
