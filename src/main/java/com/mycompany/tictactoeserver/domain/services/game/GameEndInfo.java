/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;

/**
 *
 * @author sheri
 */
public class GameEndInfo {

    private String roomId;
    private String winnerId; // If this is null, it means DRAW

    public GameEndInfo() {
    }

    public String getRoomId() {
        return roomId;
    }

    public String getWinnerId() {
        return winnerId;
    }
}
