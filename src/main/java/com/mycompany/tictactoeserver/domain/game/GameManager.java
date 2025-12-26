package com.mycompany.tictactoeserver.domain.game;

import java.util.UUID;

public interface GameManager {
    void createRoom();

    void addPlayer(UUID uuid);
}