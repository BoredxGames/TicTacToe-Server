package com.mycompany.tictactoeserver.domain.utils.exception;

public class SessionNotFoundException extends Exception {
    public SessionNotFoundException(StackTraceElement[] stackTraceElements) {
        super("session-not-found-exception");
        setStackTrace(stackTraceElements);
    }

    public SessionNotFoundException() {
        super("session-not-found-exception");
    }
}
