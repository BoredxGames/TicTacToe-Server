/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.entity;

/**
 *
 * @author Hazem
 */
public class PlayerRow {
    private final String username ; 
    private final String status ;

    public PlayerRow(String username, String Status) {
        this.username = username;
        this.status = Status;
    }


    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "PlayerRow{" + "username=" + username + ", Status=" + status + '}';
    }

    public PlayerRow(String username) {
        this.username = username;
        this.status = "Offline";
    }
   
}
