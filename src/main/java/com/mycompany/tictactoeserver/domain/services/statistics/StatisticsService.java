package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.services.player.PlayerService;
import com.mycompany.tictactoeserver.domain.services.playerSession.PlayerSessionService;

import java.util.*;
import java.util.List;
/**
 *
 * @author Tasneem
 */
public class StatisticsService {
    private final PlayerSessionService playerSessionService;
    private final PlayerService playerService;
    private final GameServerManager gameServerManager;

    public StatisticsService(PlayerService playerService, GameServerManager gameServerManager, GameServerManager gameServerManager1) {
        this.playerService = playerService;
        this.gameServerManager = gameServerManager1;
        this.playerSessionService = new PlayerSessionService();
    }

    public List<ActivityPoint> getAllPlayerSessions() {
        List<Session> sessions = playerSessionService.getAllPlayerSessions();

        return new ArrayList<>();
    }

    public List<PlayerEntity> getLeaderboard() {
        List<Player> players = playerService.getAllPlayers();

        List<PlayerEntity> leaderboard = new ArrayList<>();
        for (Player player : players) {
            PlayerEntity playerEntity = new PlayerEntity(
                    player.getUsername(),
                    player.getScore()
            );
            leaderboard.add(playerEntity);
        }

        leaderboard.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        return leaderboard;
    }

    public int getTotalPlayersCount() {
        return playerService.getAllPlayers().size();
    }

    public int getOnlinePlayersCount() {
        return gameServerManager.getOnlinePlayersCount();
    }
}
