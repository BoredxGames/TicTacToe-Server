package com.mycompany.tictactoeserver.domain.entity;

/**
 *
 * @author Tasneem
 */
public class ActivityPoint {
    private int hour;
    private int playerCount;          
    
    public ActivityPoint(int hour, int playerCount) {
        this.hour = hour;
        this.playerCount = playerCount;
    }
    
    public int getHour() {
        return hour;
    }
    
    public void setHour(int hour) {
        this.hour = hour;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}