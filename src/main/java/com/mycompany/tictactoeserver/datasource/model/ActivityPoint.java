package com.mycompany.tictactoeserver.datasource.model;


import java.time.LocalDateTime;

public class Activity {

    private String playerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Activity() {
        this.startTime = LocalDateTime.now();
    }

    public Activity(String playerId) {
         this.startTime = LocalDateTime.now();
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
