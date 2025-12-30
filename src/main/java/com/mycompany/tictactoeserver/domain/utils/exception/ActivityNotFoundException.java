package com.mycompany.tictactoeserver.domain.utils.exception;

public class ActivityNotFoundException extends Exception {
    public ActivityNotFoundException(StackTraceElement[] stackTraceElements) {
        super("activity-not-found-exception");
        setStackTrace(stackTraceElements);
    }

    public ActivityNotFoundException() {
        super("activity-not-found-exception");
    }
}
