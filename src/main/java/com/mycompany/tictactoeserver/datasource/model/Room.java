package com.mycompany.tictactoeserver.datasource.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Room {

    private String id;
    private String firstPlayerId;
    private String secondPlayerId;
    private String winnerId;
    private int status;
     private LocalDateTime createdAt;

    public Room() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
 public Room(String firstPlayerId ) {
       this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.firstPlayerId = firstPlayerId;
    }
    public Room(String firstPlayerId, String secondPlayerId) {
       this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
    }

    public String getId() {
        return id;
    }


    public String getFirstPlayerId() {
        return firstPlayerId;
    }

    public void setFirstPlayerId(String firstPlayerId) {
        this.firstPlayerId = firstPlayerId;
    }

    public String getSecondPlayerId() {
        return secondPlayerId;
    }

    public void setSecondPlayerId(String secondPlayerId) {
        this.secondPlayerId = secondPlayerId;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
