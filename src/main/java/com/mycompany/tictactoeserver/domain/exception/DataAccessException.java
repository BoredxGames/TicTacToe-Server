package com.mycompany.tictactoeserver.domain.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(StackTraceElement[] stackTraceElements) {
        super("data-access-exception");
        setStackTrace(stackTraceElements);
    }
}