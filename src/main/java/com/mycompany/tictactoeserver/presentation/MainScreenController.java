package com.mycompany.tictactoeserver.presentation;

import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Button centerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void toggleServer() {
        GameServerManager serverManager = GameServerManager.getInstance();

        try {
            if (!serverManager.isRunning()) {
                serverManager.start();
                centerButton.setText("Stop Server");
                return;
            }

            serverManager.stop();
            centerButton.setText("Start Server");
        } catch (Exception e) {
            System.out.println("Msg: " + e.getMessage());
            System.out.println("Trace: " + Arrays.toString(e.getStackTrace()));
            showAlert(Alert.AlertType.ERROR, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert dialog = new Alert(type);
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        dialog.showAndWait();
    }

}
