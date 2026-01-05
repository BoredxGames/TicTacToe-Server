package com.mycompany.tictactoeserver.domain.services.game;

public interface GameManager {
    void createRoom();

    void addPlayer(String uuid);
}