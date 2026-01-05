package com.mycompany.tictactoeserver.domain.utils.callbacks;

import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;

@FunctionalInterface
public interface PlayerHandlerCallback {
    void call(PlayerConnectionHandler player);
}
