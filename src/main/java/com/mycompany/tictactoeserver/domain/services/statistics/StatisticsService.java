package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;
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

    public StatisticsService(PlayerService playerService) {
        this.playerService = playerService;
        this.playerSessionService = new PlayerSessionService();
    }

    public List<ActivityPoint> getAllPlayerSessions() {
        List<Session> sessions = playerSessionService.getAllPlayerSessions();

        List<ActivityPoint> activityPoints = new ArrayList<>();
        return activityPoints;
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
}
