package com.mycompany.tictactoeserver.datasource.model;
import java.util.UUID;


import java.time.LocalDateTime;
public class ActivityPoint {
   private final String id;
    private String playerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public ActivityPoint() {
        this.id = UUID.randomUUID().toString();
        this.startTime = LocalDateTime.now();
    }

    public ActivityPoint(String playerId) {
         this.id = UUID.randomUUID().toString();
         this.startTime = LocalDateTime.now();
        this.playerId = playerId;
    }
    public String getId() {
        return id;
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


