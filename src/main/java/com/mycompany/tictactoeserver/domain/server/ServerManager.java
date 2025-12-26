package com.mycompany.tictactoeserver.domain.server;

import java.net.Socket;

public interface ServerManager {
    void start();

    void stop();

    void broadcastMessage(String message);

    void sendMessage(String message, Socket socket);

    void parseMessage(String message);
}