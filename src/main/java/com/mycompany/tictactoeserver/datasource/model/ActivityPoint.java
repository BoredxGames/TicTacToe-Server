package com.mycompany.tictactoeserver.datasource.model;

import java.time.LocalDateTime;

public class ActivityPoint {
    private String id;
    private String playerId;
    private LocalDateTime startActivityDate;
    private LocalDateTime endActivityDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public LocalDateTime getStartActivityDate() {
        return startActivityDate;
    }

    public void setStartActivityDate(LocalDateTime startActivityDate) {
        this.startActivityDate = startActivityDate;
    }

    public LocalDateTime getEndActivityDate() {
        return endActivityDate;
    }

    public void setEndActivityDate(LocalDateTime endActivityDate) {
        this.endActivityDate = endActivityDate;
    }
}