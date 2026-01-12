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
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

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

    public List<ActivityPoint> getLast5MinutesPoints() {
        System.out.println("--- Starting calculation for Last 5 Minutes ---");
        return calculateTrafficPoints(5, ChronoUnit.MINUTES);
    }

    public List<ActivityPoint> getLast30MinutesPoints() {
        System.out.println("--- Starting calculation for Last 30 Minutes ---");
        return calculateTrafficPoints(30, ChronoUnit.MINUTES);
    }

    public List<ActivityPoint> getLast24HoursPoints() {
        System.out.println("--- Starting calculation for Last 24 Hours ---");
        return calculateTrafficPoints(24, ChronoUnit.HOURS);
    }

    private List<ActivityPoint> calculateTrafficPoints(int rangeAmount, ChronoUnit unit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime rangeStart = now.minus(rangeAmount, unit);

        List<Session> sessions = playerSessionService.getSessionsByDateRange(rangeStart, now);

        if (sessions.isEmpty()) {
            System.out.println("No sessions found for the last " + rangeAmount + " " + unit + ". Returning empty list.");
            return new ArrayList<>();
        }

        System.out.println("Found " + sessions.size() + " total sessions. Processing buckets...");

        List<ActivityPoint> activityPoints = new ArrayList<>();

        for (int i = 0; i < rangeAmount; i++) {
            LocalDateTime bucketStart = rangeStart.plus(i, unit);
            LocalDateTime bucketEnd = bucketStart.plus(1, unit);

            int playersOnline = countOverlappingSessions(sessions, bucketStart, bucketEnd, now);

            int timeLabel = (unit == ChronoUnit.HOURS) ? bucketStart.getHour() : bucketStart.getMinute();

            activityPoints.add(new ActivityPoint(timeLabel, playersOnline));
        }

        System.out.println("Finished. Generated " + activityPoints.size() + " points.");
        if (!activityPoints.isEmpty()) {
            System.out.println("Latest point player count: " + activityPoints.get(activityPoints.size() - 1).getPlayerCount());
        }

        return activityPoints;
    }

    private int countOverlappingSessions(List<Session> sessions, LocalDateTime bucketStart, LocalDateTime bucketEnd, LocalDateTime now) {
        int count = 0;
        ArrayList<String> uniquePlayersIDs = new ArrayList<>();
        for (Session session : sessions) {
            LocalDateTime sessionStart = session.getStartTime();
            LocalDateTime sessionEnd = (session.getEndTime() != null) ? session.getEndTime() : now;
            String playerID = session.getPlayerId();

            if (sessionStart.isBefore(bucketEnd) && sessionEnd.isAfter(bucketStart) && !uniquePlayersIDs.contains(playerID)) {
                uniquePlayersIDs.add(playerID);
                count++;
            }
        }
        return count;
    }

    public List<PlayerEntity> getLeaderboard() {
        List<Player> players = playerService.getAllPlayers();

        List<PlayerEntity> leaderboard = new ArrayList<>();
        for (Player player : players) {
            PlayerEntity playerEntity = new PlayerEntity(
                    player.getId(),
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

    public int getAvailablePlayerCount() {
        return gameServerManager.getAvailablePlayersCount();
    }

    public int getConnectedPlayersCount() {
        return gameServerManager.getConnectedPlayersCount();
    }

    public int getInGamePlayersCount() {
        return gameServerManager.getInGamePlayersCount();
    }
}
