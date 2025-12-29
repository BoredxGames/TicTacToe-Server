/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.exception;

/**
 *
 * @author sheri
 */
public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(StackTraceElement[] stackTrace) {
        super("player-not-found-exception");
        super.setStackTrace(stackTrace);
    }
}