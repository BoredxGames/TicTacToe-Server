package com.mycompany.tictactoeserver.domain.service;

import com.mycompany.tictactoeserver.datasource.database.dao.PlayerDAO;
import com.mycompany.tictactoeserver.datasource.model.Player;

import java.util.List;

/**
 *
 * @author Tasneem
 */
public class PlayerService {
    private final PlayerDAO playerDAO;

    public PlayerService(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    public List<Player> getAllPlayers() {
        return playerDAO.findAll();
    }
}
