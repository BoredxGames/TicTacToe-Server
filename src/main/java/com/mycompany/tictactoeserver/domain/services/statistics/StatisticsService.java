package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.database.dao.PlayerDAO;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.services.player.PlayerService;
import com.mycompany.tictactoeserver.domain.services.playerSession.PlayerSessionService;

import java.time.LocalDateTime;
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

    public StatisticsService() {
        this.playerService = new PlayerService(new PlayerDAO());
        this.gameServerManager = GameServerManager.getInstance();
        this.playerSessionService = new PlayerSessionService();
    }

    public List<ActivityPoint> getOnlinePlayersCountPerHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);

        List<Session> recentSessions = playerSessionService.getSessionsByDateRange(twentyFourHoursAgo, now);

        if (recentSessions.isEmpty()) {
            return new ArrayList<>();
        }

        List<ActivityPoint> activityPoints = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            LocalDateTime hourStart = twentyFourHoursAgo.plusHours(i).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime hourEnd = hourStart.plusHours(1);
            int playersOnline = 0;

            for (Session session : recentSessions) {
                LocalDateTime sessionStart = session.getStartTime();
                LocalDateTime sessionEnd = session.getEndTime() != null ? session.getEndTime() : now;

                if (sessionStart.isBefore(hourEnd) && sessionEnd.isAfter(hourStart)) {
                    playersOnline++;
                }
            }

            activityPoints.add(new ActivityPoint(hourStart.getHour(), playersOnline));
        }

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

    public int getOnlinePlayersCount() {
        return gameServerManager.getOnlinePlayersCount();
    }

    public int getOfflinePlayersCount() {
        return getTotalPlayersCount() - getOnlinePlayersCount();
    }
}
