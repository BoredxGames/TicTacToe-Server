package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.model.ActivityPoint;
import com.mycompany.tictactoeserver.datasource.model.Player;

import java.util.Vector;

public interface StatisticsManager {
    Vector<ActivityPoint> getActivity();

    Vector<Player> getLeaderboard();
}
