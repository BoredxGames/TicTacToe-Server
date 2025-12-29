package com.mycompany.tictactoeserver;

import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.domain.exception.DatabaseConnectionException;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;
import java.io.IOException;
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
        try {
            Database db = Database.getInstance();
            db.connect();
            scene = new Scene(loadFXML("presentation/main_screen"), 640, 480);
            stage.setScene(scene);
            stage.show();
        } catch (DatabaseConnectionException ex) {
            System.getLogger(App.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL resource = App.class.getResource(fxml + ".fxml");
        if (resource == null) {
            throw new IOException("Cannot load FXML: " + fxml);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof Exception) {
                ExceptionHandlerMiddleware.getInstance().handleException((Exception) throwable);
            } else {
                throwable.printStackTrace();
            }
        });

        launch();
    }
}