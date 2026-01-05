/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.utils.exception;

/**
 *
 * @author sheri
 */
public class DatabaeDisConnectionException extends Exception {
    public DatabaeDisConnectionException(StackTraceElement[] stackTrace) {
        super("database-dis-connection-exception");
        super.setStackTrace(stackTrace);
    }
}
