package com.mycompany.tictactoeserver.domain.utils.exception;

public class DataAccessException extends Exception {
    public DataAccessException(StackTraceElement[] stackTraceElements) {
        super("data-access-exception");
        setStackTrace(stackTraceElements);
    }
}