/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.domain.entity;

/**
 *
 * @author mahmoud
 */
public enum PlayerStatus {
    OFFLINE(10),
    ONLINE(20),
    PENDING(30), 
    IN_GAME(40);

    private final int id;

    PlayerStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static PlayerStatus valueOf(int id) {
        for (PlayerStatus status : PlayerStatus.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        return null;
    }

    public static PlayerStatus fromString(String status) {
        return switch (status.toLowerCase()) {
            case "offline" -> OFFLINE;
            case "online" -> ONLINE;
            case "pending" -> PENDING;
            case "in_game" -> IN_GAME;
            default -> throw new AssertionError();
        };
    }
}
