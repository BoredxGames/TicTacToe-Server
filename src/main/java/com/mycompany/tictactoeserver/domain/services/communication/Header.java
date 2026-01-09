/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.communication;

/**
 *
 * @author Hazem
 */

public class Header {

    private MessageType msgType;
    private Action action;

    public Header() {
    }

    public Header(MessageType msgType, Action action) {
        this.msgType = msgType;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Header{" + "msgType=" + msgType + ", action=" + action + '}';
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(MessageType msgType) {
        this.msgType = msgType;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
