/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;

/**
 *
 * @author mahmoud
 */
public class GameResponseInfo {
       private String requesterId;
    private String responderId;
    private String responderUserName;
    private boolean accepted;

    public String getRequesterId() {
        return requesterId;
    }

    public GameResponseInfo(String requesterId, String responderId, String responderUserName, boolean accepted) {
        this.requesterId = requesterId;
        this.responderId = responderId;
        this.responderUserName = responderUserName;
        this.accepted = accepted;
    }

    public String getResponderUserName() {
        return responderUserName;
    }

    public void setResponderUserName(String responderUserName) {
        this.responderUserName = responderUserName;
    }


    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
    
    
}
