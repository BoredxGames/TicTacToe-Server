/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;
import java.util.Vector;

/**
 *
 * @author mahmoud 
 */
public class AvailablePlayersInfo {
 public Vector<PlayerEntity> onlinePlayers;
    public Vector<PlayerEntity> inGamePlayers;
    public Vector<PlayerEntity> pendingPlayers;

    public AvailablePlayersInfo(Vector<PlayerEntity> online, Vector<PlayerEntity> inGame, Vector<PlayerEntity> pending) {
        this.onlinePlayers = online;
        this.inGamePlayers = inGame;
        this.pendingPlayers = pending;
    }
}