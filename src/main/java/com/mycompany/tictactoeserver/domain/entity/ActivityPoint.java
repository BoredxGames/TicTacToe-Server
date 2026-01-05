package com.mycompany.tictactoeserver.domain.entity;

import java.time.LocalDateTime;
/**
 *
 * @author Tasneem
 */
public class ActivityPoint {
    private LocalDateTime timestamp;  
    private int playerCount;          
    
    public ActivityPoint(LocalDateTime timestamp, int playerCount) {
        this.timestamp = timestamp;
        this.playerCount = playerCount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}