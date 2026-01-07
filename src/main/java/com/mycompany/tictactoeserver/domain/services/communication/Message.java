package com.mycompany.tictactoeserver.domain.services.communication;

import com.google.gson.Gson;




/**
 *
 * @author Hazem
 **/

public class Message {
    private Header header = null;
    private String data = null;

    public Message() {
    }

    public Message(Header header, String data) {
        this.header = header;
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Message{" + "header=" + header + ", data=" + data + '}';
    }

    public static Message createMessage(MessageType type , Action action , Object data)
    {
        return new Message(new Header(type , action), toJson(data));
    }

    static private String toJson(Object data)
    {
        Gson gson = new Gson();
        return  gson.toJson(data);

    }
}

