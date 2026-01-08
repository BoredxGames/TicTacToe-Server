/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.communication;

/**
 *
 * @author Hazem
 */
public enum MessageType {
    REQUEST(10),
    RESPONSE(20),
    EVENT(30),
    ERROR(40);

    final int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MessageType valueOf(int id) {
        for (MessageType type : MessageType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }

        return null;
    }
    public static MessageType fromString(String msgType)
    {
        return switch (msgType.toLowerCase()) {
            case "request" -> MessageType.REQUEST;
            case "response"->MessageType.RESPONSE;
            case "event"->MessageType.EVENT;
            case "error"-> MessageType.ERROR;
     
            default -> throw new AssertionError();
        };
     }
}




