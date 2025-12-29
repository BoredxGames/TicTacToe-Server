/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.exception;

/**
 *
 * @author sheri
 */
public class PlayerUpdateException extends Exception {
    public PlayerUpdateException(StackTraceElement[] stackTrace) {
        super("player-update-exception");
        super.setStackTrace(stackTrace);
    }
}
