package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.domain.exception.*;

public interface ServerManager {
    void start() ;

    void stop() throws StopServerException;

    void broadcastMessage(String message) throws PlayerSendMessageException;

    void sendMessage(String message, PlayerConnectionHandler player) throws PlayerSendMessageException;

    void parseMessage(String message);

    void addListener(PlayerConnectionHandler listener);

    void removeListener(PlayerConnectionHandler listener);

}