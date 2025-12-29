/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.exception;

/**
 *
 * @author sheri
 */
public class PlayerDeletionException extends Exception {
    public PlayerDeletionException(StackTraceElement[] stackTrace) {
        super("player-deletion-exception");
        super.setStackTrace(stackTrace);
    }
}
