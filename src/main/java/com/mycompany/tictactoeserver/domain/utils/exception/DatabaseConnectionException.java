/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.utils.exception;

/**
 *
 * @author sheri
 */
public class DatabaseConnectionException extends Exception {

    public DatabaseConnectionException(StackTraceElement[] stackTrace) {
        super("database-connection-exception");
        super.setStackTrace(stackTrace);
    }
}
