/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.services.game;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;

/**
 *
 * @author mahmoud 
 */


public class GameRequest {
    public final PlayerConnectionHandler requester;
    public final PlayerConnectionHandler target;

    public GameRequest(PlayerConnectionHandler requester,
                       PlayerConnectionHandler target) {
        this.requester = requester;
        this.target = target;
    }
}