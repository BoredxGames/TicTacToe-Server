package com.mycompany.tictactoeserver.domain.exception;

public class PlayerReceiveMessageException extends Exception {

    public PlayerReceiveMessageException(StackTraceElement[] stackTrace) {
        super("player-receive-message-exception");
        super.setStackTrace(stackTrace);
    }
}