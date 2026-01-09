/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;

/**
 *
 * @author mahmoud
 */
public class MoveInfo {
    private final String roomId;
    private final String playerId;
    private final Object move;

    public MoveInfo(String playerId, Object move, String roomId) {
        this.playerId = playerId;
        this.move = move;
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public Object getMove() {
        return move;
    }
    
}
