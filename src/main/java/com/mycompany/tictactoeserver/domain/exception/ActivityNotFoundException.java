package com.mycompany.tictactoeserver.domain.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(StackTraceElement[] stackTraceElements) {
        super("activity-not-found-exception");
        setStackTrace(stackTraceElements);
    }

    public ActivityNotFoundException(String activityId) {
        super("activity-not-found-exception for activity " + activityId);
    }
}
