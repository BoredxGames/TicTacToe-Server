/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import java.util.UUID;
/**
 *
 * @author mahmoud 
 */
public class GameRoom {

    private final String roomId;
    private final PlayerConnectionHandler player1;
    private final PlayerConnectionHandler player2;

    public GameRoom(PlayerConnectionHandler player1, PlayerConnectionHandler player2) {
        this.roomId = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean hasPlayer(PlayerConnectionHandler player) {
        return player == player1 || player == player2;
    }

    public PlayerConnectionHandler getOpponent(PlayerConnectionHandler player) {
        if (player == player1) return player2;
        if (player == player2) return player1;
        return null;
    }
}
