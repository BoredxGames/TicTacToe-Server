/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.utils.exception;

/**
 *
 * @author mahmoud
 */
public class RoomCreationException extends Exception {

    public RoomCreationException(StackTraceElement[] stackTrace) {
        super("room-creation-exception");
        super.setStackTrace(stackTrace);
    }

}
