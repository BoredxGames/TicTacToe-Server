/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;

/**
 *
 * @author mahmoud
 */
public class GameRequestInfo {
    private final String requesterId;
    private final String targetId;
  

    public GameRequestInfo(String requesterId, String targetId) {
        this.requesterId = requesterId;
        this.targetId = targetId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public String getTargetId() {
        return targetId;
    }
    
}