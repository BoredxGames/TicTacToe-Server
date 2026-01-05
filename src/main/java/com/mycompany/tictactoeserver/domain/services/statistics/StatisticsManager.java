package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.ActivityPoint;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;

import java.util.List;

public interface StatisticsManager {
    List<ActivityPoint> getAllPlayerSessions();
    List<PlayerEntity> getLeaderboard();
    int getTotalPlayersCount();
}
