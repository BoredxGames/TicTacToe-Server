package com.mycompany.tictactoeserver.domain.exception;

public class ActiveSessionExistsException extends Exception {
    public ActiveSessionExistsException(StackTraceElement[] stackTraceElements) {
        super("active-session-exists-exception");
        setStackTrace(stackTraceElements);
    }

    public ActiveSessionExistsException(String[] exceptionData) {
        super("active-session-exists-exception for player " + exceptionData[0] + " in session " + exceptionData[1] + ".");
    }
}
