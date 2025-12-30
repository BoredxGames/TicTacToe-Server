package com.mycompany.tictactoeserver.domain.exception;

import com.mycompany.tictactoeserver.datasource.model.Player;

import java.util.Arrays;

public class ExceptionHandlerMiddleware {

    // Singleton
    private static ExceptionHandlerMiddleware instance;

    private ExceptionHandlerMiddleware() {
    }

    public static ExceptionHandlerMiddleware getInstance() {
        if (instance == null)
            instance = new ExceptionHandlerMiddleware();
        return instance;
    }

    public void handleException(Exception ex) {

        switch (ex.getMessage()) {
            case "start-server-exception":
            case "stop-server-exception":
            case "player-connection-exception":
            case "player-receive-message-exception":
            case "player-send-message-exception":
            case "database-connection-exception":
            case "database-dis-connection-exception":
            case "hashing-exception":

                System.out.println("hashing-exception");
                break;
            case "room-not-found-exception":
                System.out.println("room-not-found-exception");
                break;
            case "room-updation-exception":
                System.out.println("room-updation-exception");
                break;
            case "room-creation-exception":
                System.out.println("room-creation-exception");
                break;
            case "player-insertion-exception":
            case "player-update-exception":
                System.out.println("player-update-exception");
                break;
            case "player-deletion-exception":
                System.out.println("player-deletion-exception");
                break;
            case "player-not-found-exception":
                System.out.println("player-not-found-exception");
                break;
            case "data-access-exception":
                System.out.println("data-access-exception");
                break;
            default:
        }

        logError(ex.getMessage(), ex.getStackTrace());
    }

    public void handleException(Exception ex, String[] data) {
        switch (ex.getMessage()) {
            case "active-session-exists-exception":
                System.out.println("active-session-exists-exception for player " + data[0] + " in session " + data[1] + ".");
                break;
            case "session-not-found-exception":
                System.out.println("session-not-found-exception for activity " + data[0]);
                break;
            default:
                System.out.println("unknown-exception");
                break;
        }
        logError(ex.getMessage(), ex.getStackTrace());
    }

    private void logError(String message, StackTraceElement[] stackTrace) {
        System.out.println(message + ": ---> " + Arrays.toString(stackTrace));
    }
}
