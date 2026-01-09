/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;

/**
 *
 * @author moham
 */
public class GameStartInfo {
    private final String roomId;
    private final String player1;
    private final String player2;

    public GameStartInfo(String roomId, String player1, String player2) {
        this.roomId = roomId;
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }
    
}
