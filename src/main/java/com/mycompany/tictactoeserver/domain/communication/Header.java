/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.communication;

/**
 *
 * @author Hazem
 */

public class Header {
     
    private MessageType msgType ;

    public Header() {
    }

    public Header(MessageType msgType) {
        this.msgType = msgType;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }
     
}
