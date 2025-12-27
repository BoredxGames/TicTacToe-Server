package com.mycompany.tictactoeserver.domain.exception;

public class PlayerConnectionException extends Exception {

    public PlayerConnectionException(StackTraceElement[] stackTrace) {
        super("player-connection-exception");
        super.setStackTrace(stackTrace);
    }
}