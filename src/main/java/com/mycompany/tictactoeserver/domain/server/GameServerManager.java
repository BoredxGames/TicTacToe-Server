package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.domain.exception.*;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class GameServerManager implements ServerManager {
    private volatile ServerSocket serverSocket;
    private volatile Vector<PlayerConnectionHandler> players = new Vector<>();
    public volatile Thread thread;
    private volatile boolean isRunning = false;

    private static GameServerManager instance;

    public static synchronized GameServerManager getInstance() {
        if (instance == null)
            instance = new GameServerManager();
        return instance;
    }

    private GameServerManager() {
    }

    @Override
    public void start()
    {
        if(isRunning)
            return;

        isRunning = true;

        thread = new Thread ( () -> {
            try {
                launchServer();
            } catch (ServerInterruptException | PlayerConnectionException e) {
                ExceptionHandlerMiddleware.getInstance().handleException(e);
            }
        });

        thread.start();
    }

    @Override
    public void stop() throws StopServerException {
        if(!isRunning)
            return;

        isRunning = false;

        thread.interrupt();
        thread = null;
        stopServer();
    }

    private void launchServer() throws ServerInterruptException, PlayerConnectionException {
        try {
            System.out.println("Starting GameServerManager");
            int PORT = 4321;
            serverSocket = new ServerSocket(PORT);

            while (true) {
                System.out.println("Listening GameServerManager");
                Socket clientSocket = serverSocket.accept();
                players.add(new PlayerConnectionHandler(clientSocket));
            }

        } catch (PlayerConnectionException ex) {
            throw ex;
        } catch (IOException e) {
            throw new ServerInterruptException(e.getStackTrace());
        }
    }

    private void stopServer() throws StopServerException {
        System.out.println("Stopping GameServerManager");
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new StopServerException(e.getStackTrace());
            }
        }

        for (PlayerConnectionHandler player : players) {
            removeListener(player);

        }
    }

    @Override
    public void broadcastMessage(String message) throws PlayerSendMessageException {
        for (PlayerConnectionHandler player : players) {
            sendMessage(message, player);
        }

    }

    @Override
    public void sendMessage(String message, PlayerConnectionHandler player) throws PlayerSendMessageException {
        player.sendMessage(message);

    }

    @Override
    public void parseMessage(String message) {
        JSONObject json = new JSONObject(message);

        mapFunction(json);
    }

    @Override
    public synchronized void addListener(PlayerConnectionHandler player) {
        players.add(player);
    }

    @Override
    public synchronized void removeListener(PlayerConnectionHandler player) {
        player.close();
        players.remove(player);
    }


    private void mapFunction(JSONObject json) {
        String function = json.getString("function");

        switch (function) {
        }
    }
}
