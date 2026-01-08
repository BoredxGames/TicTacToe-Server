package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.domain.services.communication.MessageRouter;
import com.mycompany.tictactoeserver.domain.utils.callbacks.PlayerHandlerCallback;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerSendMessageException;
import com.mycompany.tictactoeserver.domain.utils.exception.ServerInterruptException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class GameServerManager {
    private final Vector<PlayerConnectionHandler> players = new Vector<>();
    public Thread thread;
    private final ServerRunnable runnable;

    private final Object lock = new Object();

    private static GameServerManager instance;

    public static GameServerManager getInstance() {
        if (instance == null) instance = new GameServerManager();
        return instance;
    }

    private GameServerManager() {
        runnable = new ServerRunnable(this::addPlayer);
        thread = new Thread(runnable);

        thread.start();
    }

    public boolean isRunning() {
        return runnable.isRunning();
    }

    public void start() {
        synchronized (lock) {
            System.out.println("Starting GameServerManager");
            runnable.toggleRunning();
        }
    }

    public void stop() {
        System.out.println("Stopping GameServerManager");
        runnable.toggleRunning();

        Vector<PlayerConnectionHandler> copy;
        synchronized (lock) {
            copy = new Vector<>(players);
            players.clear();
        }

        for (PlayerConnectionHandler p : copy) {
            p.close();
        }
    }


    public void broadcastMessage(String message) throws PlayerSendMessageException {
        synchronized (lock) {
            for (PlayerConnectionHandler player : players) {
                sendMessage(message, player);
            }
        }

    }

    public void sendMessage(String message, PlayerConnectionHandler player) throws PlayerSendMessageException {
        player.sendMessageToPlayer(message);

    }

    public void onReceiveMessage(String message, PlayerConnectionHandler sender) {
        try {
            MessageRouter router = MessageRouter.getInstance();
            System.out.println("testingggggggggg");
            router.navigateMessage(message, sender);
        } catch (PlayerSendMessageException ex) {
            System.getLogger(GameServerManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void addPlayer(PlayerConnectionHandler player) {
        synchronized (lock) {
            players.add(player);
        }
    }

    public void removePlayer(PlayerConnectionHandler player) {
        synchronized (lock) {
            players.remove(player);
        }
    }

    public int getOnlinePlayersCount() {
        synchronized (lock) {
            return players.size();
        }
    }
}

class ServerRunnable implements Runnable {
    private volatile ServerSocket serverSocket;
    PlayerHandlerCallback onAdd;

    private volatile boolean isRunning = false;

    int PORT = 4321;

    ServerRunnable(PlayerHandlerCallback onAdd) {
        this.onAdd = onAdd;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            ServerInterruptException interruptException = new ServerInterruptException(e.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(interruptException);
        }
    }

    public synchronized void toggleRunning() {
        isRunning = !isRunning;
        if (isRunning) {
            notify();
            System.out.println("Server Notified");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {

        while (true) {
            try {
                synchronized (this) {
                    if (!isRunning) {
                        System.out.println("Server Waiting...");
                        serverSocket.close();
                        wait();
                        serverSocket = new ServerSocket(PORT);
                    }
                }

                System.out.println("Server Accepting...");
                Socket clientSocket = serverSocket.accept();
                if (isRunning) {
                    PlayerConnectionHandler player = new PlayerConnectionHandler(clientSocket);
                    onAdd.call(player);
                    System.out.println("Client Connected: " + clientSocket.getInetAddress().getHostAddress());
                    
                   
                } else {
                    clientSocket.close();
                }
            } catch (SocketException | InterruptedException ex) {
                System.out.println("Server Closed, Exception: ---> " + ex.getMessage());
            } catch (IOException e) {
                ServerInterruptException customException = new ServerInterruptException(e.getStackTrace());
                ExceptionHandlerMiddleware.getInstance().handleException(customException);
            }
        }
    }
}
