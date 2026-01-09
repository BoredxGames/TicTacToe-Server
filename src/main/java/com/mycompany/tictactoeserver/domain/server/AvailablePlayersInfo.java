/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.datasource.model.Player;
import java.util.Vector;

/**
 *
 * @author moham
 */
public class AvailablePlayersInfo {
    public Vector<Player> players;

    public AvailablePlayersInfo(Vector<Player> players) {
        this.players = players;
    }
}