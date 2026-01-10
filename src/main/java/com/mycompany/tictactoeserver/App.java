package com.mycompany.tictactoeserver;

import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.domain.services.communication.MessageRouter;
import com.mycompany.tictactoeserver.domain.utils.DeviceManager;
import com.mycompany.tictactoeserver.domain.utils.exception.DatabaseConnectionException;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        setupDependencies();
        scene = new Scene(loadFXML("dashboard"), 1600, 900);
        stage.setScene(scene);
        stage.show();
    }


    private Parent loadFXML(String fxml) throws IOException {

        return new FXMLLoader(
                App.class.getResource("/presentation/" + fxml + ".fxml")
        ).load();
    }

    static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof Exception exception) {
                ExceptionHandlerMiddleware.getInstance().handleException(exception);
            } else {
                throwable.printStackTrace();
            }
        });


        launch();
    }

    private void setupDependencies() {
        try {
            Database.getInstance().connect();
            DeviceManager.getIpv4Address();
        } catch (DatabaseConnectionException | SocketException e) {
            ExceptionHandlerMiddleware.getInstance().handleException(e);
        }

        MessageRouter.getInstance();
    }
}