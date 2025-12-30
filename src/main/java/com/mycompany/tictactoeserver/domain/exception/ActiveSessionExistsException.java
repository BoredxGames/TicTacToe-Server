package com.mycompany.tictactoeserver.domain.exception;

public class ActiveSessionExistsException extends Exception {
    public ActiveSessionExistsException(StackTraceElement[] stackTraceElements) {
        super("active-session-exists-exception");
        setStackTrace(stackTraceElements);
    }

    public ActiveSessionExistsException() {
        super("active-session-exists-exception");
    }
}
