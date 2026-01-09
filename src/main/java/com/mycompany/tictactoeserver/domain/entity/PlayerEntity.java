/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.entity;

/**
 *
 * @author Tasneem
 */
public class PlayerEntity {
   private String id;
    private String username;
    private int score;

    public PlayerEntity(String id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
}
