package com.mycompany.tictactoeserver.domain.statistics;

import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.datasource.model.Player;

import java.util.Vector;

public interface StatisticsManager {
    Vector<Session> getActivity();
    Vector<Player> getLeaderboard();
}
