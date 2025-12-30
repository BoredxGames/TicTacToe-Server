/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.communication;

import org.json.JSONObject;

/**
 *
 * @author Hazem
 */
public class Message {
    private Header header = null;
    private JSONObject data = null;

    public Message() {
    }

    public Message(Header header, JSONObject data) {
        this.header = header;
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public JSONObject getData() {
        return data;
    }

    public JSONObject toJson() {
        JSONObject jsonMessage = new JSONObject();

        jsonMessage.put("type", this.header.getMsgType());
        jsonMessage.put("action", this.header.getAction());

        jsonMessage.put("data", this.data);


        return jsonMessage;

    }
}
