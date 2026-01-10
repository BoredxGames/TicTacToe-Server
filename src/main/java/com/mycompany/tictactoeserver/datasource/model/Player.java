package com.mycompany.tictactoeserver.datasource.model;

import java.util.UUID;

public class Player {

    final private String id;
    private String username;
    private String password;
    private int score;


    public Player() {
        this.id = UUID.randomUUID().toString();
        this.score = 0;
    }

    public Player(String username, String password) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.score = 0;
    }

    public Player(String id,String username, String password, int score) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.score = score;
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
