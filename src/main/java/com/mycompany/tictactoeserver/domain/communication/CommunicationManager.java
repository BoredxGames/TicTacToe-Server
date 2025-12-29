/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.communication;

import org.json.JSONObject;

/**
 *
 * @author Hazem
 */
public class CommunicationManager {
    
    public  static JSONObject  toJSON(Object msg)
    {
        JSONObject jsonMessage = new JSONObject(msg);
        return jsonMessage;
    }
    
     public  static String  fromJSON(JSONObject jsonMessage)
    {
        String msg = jsonMessage.toString();
        return msg;
    }
    
   
    
}
