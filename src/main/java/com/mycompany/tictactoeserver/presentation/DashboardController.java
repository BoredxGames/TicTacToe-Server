package com.mycompany.tictactoeserver.presentation;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.entity.PlayerRow;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.statistics.StatisticsService;
import com.mycompany.tictactoeserver.domain.utils.BackgroundTaskScheduler;
import com.mycompany.tictactoeserver.domain.utils.DeviceManager;
import java.net.SocketException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;


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
    private ToggleButton filterAvailableBtn;
    @FXML
    private ToggleButton filterInGameBtn;

    @FXML
    private TableView<PlayerRow> playersTable;
    @FXML
    private TableColumn<PlayerRow, String> userColumn;
    @FXML
    private TableColumn<PlayerRow, String> statusColumn;

   
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
    private BackgroundTaskScheduler chartUpdater;

    IntegerProperty availablePlayers = new SimpleIntegerProperty(0);
    IntegerProperty offlinePlayers = new SimpleIntegerProperty(0);
    IntegerProperty connectedPlayers = new SimpleIntegerProperty(0);
    IntegerProperty inGamePlayers = new SimpleIntegerProperty(0);

    private final ObservableList<PlayerRow> playersList = FXCollections.observableArrayList();
    
    private FilteredList<PlayerRow> filteredData;
    
    private final ToggleGroup filterGroup = new ToggleGroup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverManager = GameServerManager.getInstance();
        statsService = new StatisticsService();
        setupFilters();
        setupTable();
        try {
            String ip = DeviceManager.getIpv4Address();
            subtitleLabel.setText("IP Address: " + ip);
        } catch (SocketException e) {
            System.out.println("Failed to get ipv4 address in main controller");
        }

        availablePlayers.addListener((observable, oldValue, newValue) -> availableCountLabel.setText(newValue + ""));
        offlinePlayers.addListener((observable, oldValue, newValue) -> offlineCountLabel.setText(newValue + ""));
        connectedPlayers.addListener((observable, oldValue, newValue) -> connectedCountLabel.setText(newValue + ""));
        inGamePlayers.addListener((observable, oldValue, newValue) -> inGameCountLabel.setText(newValue + ""));

        offlinePlayers.addListener((observable, oldValue, newValue) -> notifyChart());
        connectedPlayers.addListener((observable, oldValue, newValue) -> {
            if (oldValue.intValue() > newValue.intValue()) {
                notifyChart();
            }
        });

        serverManager.addPlayerCountListener(this::updateMonitorStats);
        updateMonitorStats();
        notifyChart();

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
        notifyChart();
    }

    @FXML
    private void onRange30m() {
        notifyChart();
    }

    @FXML
    private void onRange24h() {
        notifyChart();
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
            refreshTableData();
        });
    }

    private void notifyChart() {
        List<ActivityPoint> points;

        int selectedMode = 30;

        if (range5mBtn.isSelected()) {
            selectedMode = 5;
        } else if (range24hBtn.isSelected()) {
            selectedMode = 24;
        }

        points = switch (selectedMode) {
            case 5 ->
                statsService.getLast5MinutesPoints();
            case 24 ->
                statsService.getLast24HoursPoints();
            default ->
                statsService.getLast30MinutesPoints();
        };

        mapDataToChart(points);

        if (chartUpdater != null) {
            chartUpdater.stopTask();
        }
        setChartUpdater(selectedMode);
        startChartUpdater(selectedMode);
    }

    private void mapDataToChart(List<ActivityPoint> points) {
        updateRangeInterval(points);
        updatePlayersCount(points);
    }

    private void updateRangeInterval(List<ActivityPoint> points) {
        lineChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Online Players");

        for (ActivityPoint point : points) {
            String label = String.valueOf(point.getHour());
            series.getData().add(new XYChart.Data<>(label, point.getPlayerCount()));
        }

        lineChart.getData().add(series);
    }

    private void updatePlayersCount(List<ActivityPoint> points) {
        int maxPlayers = points.stream()
                .mapToInt(ActivityPoint::getPlayerCount)
                .max()
                .orElse(0);

        int upperBound = maxPlayers + (maxPlayers < 5 ? 2 : 5);
        int tickUnit = Math.max(1, upperBound / 10);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(upperBound);
        yAxis.setTickUnit(tickUnit);
    }

    private void setChartUpdater(int selectedMode) {
        Runnable task;
        task = switch (selectedMode) {
            case 5 ->
                () -> {
                    Platform.runLater(() -> {
                        List<ActivityPoint> points = statsService.getLast5MinutesPoints();
                        mapDataToChart(points);
                    });
                };
            case 24 ->
                () -> {
                    Platform.runLater(() -> {
                        List<ActivityPoint> points = statsService.getLast24HoursPoints();
                        mapDataToChart(points);
                    });
                };
            default ->
                () -> {
                    Platform.runLater(() -> {
                        List<ActivityPoint> points = statsService.getLast30MinutesPoints();
                        mapDataToChart(points);
                    });
                };
        };

        if (chartUpdater == null) {
            chartUpdater = new BackgroundTaskScheduler(task);
            return;
        }

        chartUpdater.setTask(task);
    }

    private void startChartUpdater(int selectedMode) {
        switch (selectedMode) {
            case 5:
                chartUpdater.startTask(1, TimeUnit.MINUTES);
                break;
            case 24:
                chartUpdater.startTask(1, TimeUnit.HOURS);
                break;
            default:
                chartUpdater.startTask(5, TimeUnit.MINUTES);
                break;
        }
        ;
    }

    private void setupFilters() {

        filterAllBtn.setToggleGroup(filterGroup);
        filterAvailableBtn.setToggleGroup(filterGroup);
        filterInGameBtn.setToggleGroup(filterGroup);

        filterAllBtn.setSelected(true);

        filterGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase().trim();
        Toggle selectedBtn = filterGroup.getSelectedToggle();
        filteredData.setPredicate(player -> {
            if (!searchText.isEmpty() && !player.getUsername().toLowerCase().contains(searchText)) {
                return false;
            }
            if (selectedBtn == filterAvailableBtn) {
                return player.getStatus().equalsIgnoreCase("ONLINE");
            }
            if (selectedBtn == filterInGameBtn) {
                return player.getStatus().equalsIgnoreCase("IN_GAME");
            }
            return true;
        });
    }

    private void refreshTableData() {
        playersList.clear();

        Vector<PlayerConnectionHandler> connections = serverManager.getAllPlayers();

        for (PlayerConnectionHandler handler : connections) {
            Player p = handler.getPlayer();

            String username = (p != null) ? p.getUsername() : "Someone connecting 😀";
            String status = handler.getStatus().toString();

            playersList.add(new PlayerRow(username, status));
        }

    }

   private void setupTable() {
        
       
     userColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUsername()));

        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));

        filteredData = new FilteredList<>(playersList, p -> true);

        playersTable.setItems(filteredData);
        playersTable.setPlaceholder(new Label("No players found"));
    }
}
