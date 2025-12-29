package com.mycompany.tictactoeserver.domain.exception;


public class StopServerException extends Exception {

    public StopServerException(StackTraceElement[] stackTrace) {
        super("stop-server-exception");
        super.setStackTrace(stackTrace);
    }
}