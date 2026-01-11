package com.mycompany.tictactoeserver.presentation;

import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.services.statistics.StatisticsService;
import com.mycompany.tictactoeserver.domain.utils.DeviceManager;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private Label titleLabel;
    @FXML
    private Label subtitleLabel;
    @FXML
    private Label serverStatusLabel;
    @FXML
    private ToggleButton serverToggle;

    @FXML
    private Label playersTitleLabel;
    @FXML
    private TextField searchField;

    @FXML
    private ToggleButton filterAllBtn;
    @FXML
    private ToggleButton filterOnlineBtn;
    @FXML
    private ToggleButton filterOfflineBtn;
    @FXML
    private ToggleButton filterAvailableBtn;
    @FXML
    private ToggleButton filterInGameBtn;

    @FXML
    private TableView<?> playersTable;
    @FXML
    private TableColumn<?, ?> userColumn;
    @FXML
    private TableColumn<?, ?> statusColumn;
    @FXML
    private TableColumn<?, ?> gameStateColumn;
    @FXML
    private TableColumn<?, ?> lastSeenColumn;

    @FXML
    private Label paginationLabel;
    @FXML
    private Button prevPageBtn;
    @FXML
    private Button nextPageBtn;

    @FXML
    private ScrollPane monitoringScrollPane;

    @FXML
    private Label serverOverviewLabel;
    @FXML
    private GridPane overviewGrid;

    @FXML
    private Label connectedCountLabel;
    @FXML
    private Label availableCountLabel;
    @FXML
    private Label offlineCountLabel;
    @FXML
    private Label inGameCountLabel;

    @FXML
    private Label playersOverTimeLabel;
    @FXML
    private Pane playersChartPane;

    @FXML
    private ToggleButton range5mBtn;
    @FXML
    private ToggleButton range30mBtn;
    @FXML
    private ToggleButton range24hBtn;

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    private GameServerManager serverManager;
    private StatisticsService statsService;

    IntegerProperty availablePlayers = new SimpleIntegerProperty(0);
    IntegerProperty offlinePlayers = new SimpleIntegerProperty(0);
    IntegerProperty connectedPlayers = new SimpleIntegerProperty(0);
    IntegerProperty inGamePlayers = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverManager = GameServerManager.getInstance();
        statsService = new StatisticsService();

        try {
            String ip = DeviceManager.getIpv4Address();
            subtitleLabel.setText("IP Address: " + ip);
        } catch (SocketException e) {
            System.out.println("Failed to get ipv4 address in main controller");
        }

        updateMonitorStats();
        serverManager.addPlayerCountListener(this::updateMonitorStats);

        availablePlayers.addListener((observable, oldValue, newValue) -> availableCountLabel.setText(availablePlayers.get() + ""));
        offlinePlayers.addListener((observable, oldValue, newValue) -> offlineCountLabel.setText(offlinePlayers.get() + ""));
        connectedPlayers.addListener((observable, oldValue, newValue) -> connectedCountLabel.setText(connectedPlayers.get() + ""));
        inGamePlayers.addListener((observable, oldValue, newValue) -> inGameCountLabel.setText(inGamePlayers.get() + ""));


        List<ActivityPoint> points = statsService.getOnlinePlayersCountPerMinute();
        mapDataToChart(points);

    }

    @FXML
    private void onToggleServer() {
        try {
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


        } catch (Exception e) {
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
        List<ActivityPoint> points = statsService.getOnlinePlayersCountPerMinute();
        mapDataToChart(points);
    }

    @FXML
    private void onRange30m() {
        List<ActivityPoint> points = statsService.getOnlinePlayersCountPerMinute();
        mapDataToChart(points);
    }

    @FXML
    private void onRange24h() {
        List<ActivityPoint> points = statsService.getOnlinePlayersCountPerHour();
        mapDataToChart(points);
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert dialog = new Alert(type);
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        dialog.showAndWait();
    }

    private void updateMonitorStats() {
        Platform.runLater(() -> {
            availablePlayers.set(statsService.getAvailablePlayerCount());
            offlinePlayers.set(statsService.getOfflinePlayersCount());
            connectedPlayers.set(statsService.getConnectedPlayersCount());
            inGamePlayers.set(statsService.getInGamePlayersCount());

        });
    }


    /**
     * Main function that takes a list of points and updates the entire chart.
     *
     * @param points The list of data points to display.
     */
    public void mapDataToChart(List<ActivityPoint> points) {
        updateRangeInterval(points);
        updatePlayersCount(points);
    }

    /**
     * Updates the X-Axis and populates the chart series data.
     */
    private void updateRangeInterval(List<ActivityPoint> points) {
        // Clear old data
        lineChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Players");

        for (ActivityPoint point : points) {
            // Convert the int 'hour' to String for the CategoryAxis
            // You can format this string differently if 'hour' represents minutes/timestamps
            String label = String.valueOf(point.getHour());
            series.getData().add(new XYChart.Data<>(label, point.getPlayerCount()));
        }

        lineChart.getData().add(series);
    }

    /**
     * Updates the Y-Axis scaling based on the maximum player count in the list.
     */
    private void updatePlayersCount(List<ActivityPoint> points) {
        int maxPlayers = 0;

        // Find the maximum value
        for (ActivityPoint point : points) {
            if (point.getPlayerCount() > maxPlayers) {
                maxPlayers = point.getPlayerCount();
            }
        }

        // Adjust Y-Axis Logic (0 to Max + 5)
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(maxPlayers + 5);
        yAxis.setTickUnit(5);
    }
}