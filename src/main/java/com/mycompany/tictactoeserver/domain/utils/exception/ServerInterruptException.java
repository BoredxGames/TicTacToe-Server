package com.mycompany.tictactoeserver.domain.utils.exception;

public class ServerInterruptException extends Exception {
    public ServerInterruptException(StackTraceElement[] stackTrace) {
        super("server-interrupt-exception");
        super.setStackTrace(stackTrace);
    }
}
