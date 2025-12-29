package com.mycompany.tictactoeserver.domain.exception;

public class PlayerSendMessageException extends Exception {

    public PlayerSendMessageException(StackTraceElement[] stackTrace) {
        super("player-send-message-exception");
        super.setStackTrace(stackTrace);
    }
}