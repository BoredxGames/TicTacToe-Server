/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.communication;

/**
 *
 * @author Hazem
 */
public class Message {
    private Header header;
    private Object data;

    public Message() {
        header=null;
        data=null;
    }

    
    public Message(Header header, Object data) {
        this.header = header;
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public Object getData() {
        return data;
    }
}
