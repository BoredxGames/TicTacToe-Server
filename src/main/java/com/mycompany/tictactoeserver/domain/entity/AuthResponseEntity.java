package com.mycompany.tictactoeserver.domain.entity;

import com.mycompany.tictactoeserver.datasource.model.Player;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hazem
 */
public class AuthResponseEntity {

    private String id;
    private String userName;
    private int score;

    public AuthResponseEntity(String id, String userName, int Score) {
        this.id = id;
        this.userName = userName;
        this.score = Score;
    }

    public AuthResponseEntity(Player player) {
        id = player.getId();
        userName = player.getUsername();
        score = player.getScore();
    }

    public AuthResponseEntity() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setScore(int Score) {
        this.score = Score;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public int getScore() {
        return score;
    }

    public static AuthResponseEntity createAuthEntity(String id, String username, int score) {
        return new AuthResponseEntity(id, username, score);
    }

    @Override
    public String toString() {
        return "AuthRequestEntity{" + "id=" + id + ", userName=" + userName + ", Score=" + score + '}';
    }

}
