/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.utils.exception;

/**
 *
 * @author sheri
 */
public class PlayerInsertionException extends Exception {
    public PlayerInsertionException(StackTraceElement[] stackTrace) {
        super("player-insertion-exception");
        super.setStackTrace(stackTrace);
    }
}
