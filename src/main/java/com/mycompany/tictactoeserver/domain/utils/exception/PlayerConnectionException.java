package com.mycompany.tictactoeserver.domain.utils.exception;

public class PlayerConnectionException extends Exception {

    public PlayerConnectionException(StackTraceElement[] stackTrace) {
        super("player-connection-exception");
        super.setStackTrace(stackTrace);
    }
}