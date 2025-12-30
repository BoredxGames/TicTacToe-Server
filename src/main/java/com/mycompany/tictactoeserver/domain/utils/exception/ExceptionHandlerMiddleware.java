package com.mycompany.tictactoeserver.domain.utils.exception;

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
            case "room-not-found-exception":
            case "room-updation-exception":
            case "room-creation-exception":
            case "player-insertion-exception":
            case "player-update-exception":
            case "player-deletion-exception":
            case "player-not-found-exception":
            default:


        }

        logError(ex.getMessage(), ex.getStackTrace());
    }


    private void logError(String message, StackTraceElement[] stackTrace) {
        System.out.println(message + ": ---> " + Arrays.toString(stackTrace));
    }

}
