package com.mycompany.tictactoeserver.presentation;

import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.utils.DeviceManager;
import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class DashboardController implements Initializable {

    @FXML private SplitPane mainSplitPane;

    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Label serverStatusLabel;
    @FXML private ToggleButton serverToggle;

    @FXML private Label playersTitleLabel;
    @FXML private TextField searchField;

    @FXML private ToggleButton filterAllBtn;
    @FXML private ToggleButton filterOnlineBtn;
    @FXML private ToggleButton filterOfflineBtn;
    @FXML private ToggleButton filterAvailableBtn;
    @FXML private ToggleButton filterInGameBtn;

    @FXML private TableView<?> playersTable;
    @FXML private TableColumn<?, ?> userColumn;
    @FXML private TableColumn<?, ?> statusColumn;
    @FXML private TableColumn<?, ?> gameStateColumn;
    @FXML private TableColumn<?, ?> lastSeenColumn;

    @FXML private Label paginationLabel;
    @FXML private Button prevPageBtn;
    @FXML private Button nextPageBtn;

    @FXML private ScrollPane monitoringScrollPane;

    @FXML private Label serverOverviewLabel;
    @FXML private GridPane overviewGrid;

    @FXML private Label connectedCountLabel;
    @FXML private Label onlineCountLabel;
    @FXML private Label offlineCountLabel;
    @FXML private Label inGameCountLabel;

    @FXML private Label playersOverTimeLabel;
    @FXML private Pane playersChartPane;

    @FXML private ToggleButton range5mBtn;
    @FXML private ToggleButton range30mBtn;
    @FXML private ToggleButton range24hBtn;

    @FXML private Label recentActivityLabel;
    @FXML private VBox activityLogBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("DashboardController initialized");
        try {
            String ip = DeviceManager.getIpv4Address();
            subtitleLabel.setText("IP Address: " + ip);
        } catch (SocketException e) {
            System.out.println("Failed to get ipv4 address in main controller");
        }

    }

    @FXML
    private void onToggleServer() {

        GameServerManager serverManager = GameServerManager.getInstance();
        try
        {
            if (!serverManager.isRunning()) {
                serverManager.start();
                serverStatusLabel.setText("● RUNNING");
                serverStatusLabel.getStyleClass().removeAll("status-stopped");
                if (!serverStatusLabel.getStyleClass().contains("status-running")) {
                    serverStatusLabel.getStyleClass().add("status-running");
                }
                return;
            }


            serverManager.stop();
            serverStatusLabel.setText("STOPPED");
            serverStatusLabel.getStyleClass().removeAll("status-running");
            if (!serverStatusLabel.getStyleClass().contains("status-stopped")) {
                serverStatusLabel.getStyleClass().add("status-stopped");
            }


        }
        catch(Exception e)
        {
            System.out.println("Msg: " + e.getMessage());
            System.out.println("Trace: " + Arrays.toString(e.getStackTrace()));
            showAlert(Alert.AlertType.ERROR, e.getMessage(), Arrays.toString(e.getStackTrace()));
        }
    }

    @FXML
    private void onSearch() {
        System.out.println("Search triggered: " + searchField.getText());
    }

    @FXML
    private void onFilterAll() {
        System.out.println("Filter selected: ALL");
    }

    @FXML
    private void onFilterOnline() {
        System.out.println("Filter selected: ONLINE");
    }

    @FXML
    private void onFilterOffline() {
        System.out.println("Filter selected: OFFLINE");
    }

    @FXML
    private void onFilterAvailable() {
        System.out.println("Filter selected: AVAILABLE");
    }

    @FXML
    private void onFilterInGame() {
        System.out.println("Filter selected: IN-GAME");
    }

    @FXML
    private void onPreviousPage() {
        System.out.println("Previous page clicked");
    }

    @FXML
    private void onNextPage() {
        System.out.println("Next page clicked");
    }

    @FXML
    private void onRange5m() {
        System.out.println("Chart range changed: 5 minutes");
    }

    @FXML
    private void onRange30m() {
        System.out.println("Chart range changed: 30 minutes");
    }

    @FXML
    private void onRange24h() {
        System.out.println("Chart range changed: 24 hours");
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert dialog = new Alert(type);
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        dialog.showAndWait();
    }
//    private void toggleServer(){
//
//        GameServerManager serverManager = GameServerManager.getInstance();
//
//        try {
//            if (!serverManager.isRunning()) {
//                serverManager.start();
//                serverToggle.setText("Stop Server");
//                return;
//            }
//
//            serverManager.stop();
//            serverToggle.setText("Start Server");
//        } catch (Exception e) {
//            System.out.println("Msg: " + e.getMessage());
//            System.out.println("Trace: " + Arrays.toString(e.getStackTrace()));
//            showAlert(Alert.AlertType.ERROR, e.getMessage(), Arrays.toString(e.getStackTrace()));
//        }
//    }

}