package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.domain.exception.*;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.callbacks.PlayerHandlerCallback;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;

public class GameServerManager implements ServerManager {
    private final Vector<PlayerConnectionHandler> players = new Vector<>();
    public Thread thread;
    private final ServerRunnable runnable;

    private final Object lock = new Object();

    private static GameServerManager instance;

    public static GameServerManager getInstance() {
        if (instance == null) instance = new GameServerManager();
        return instance;
    }

    private GameServerManager()
    {
        runnable = new ServerRunnable(this::addListener);
        thread = new Thread(runnable);

        thread.start();
    }

    public boolean isRunning()
    {
        return runnable.isRunning();
    }

    @Override
    public synchronized void start()
    {
        System.out.println("Starting GameServerManager");
        runnable.toggleRunning();
    }

    @Override
    public void stop() {
        System.out.println("Stopping GameServerManager");
        runnable.toggleRunning();

        Vector<PlayerConnectionHandler> snapshot;
        synchronized (lock) {
            snapshot = new Vector<>(players);
            players.clear();
        }

        for (PlayerConnectionHandler p : snapshot) {
            p.close();
        }
    }





    @Override
    public void broadcastMessage(String message) throws PlayerSendMessageException
    {
        synchronized (lock) {
            for (PlayerConnectionHandler player : players)
            {
                sendMessage(message, player);
            }
        }

    }

    @Override
    public void sendMessage(String message, PlayerConnectionHandler player) throws PlayerSendMessageException
    {
        player.sendMessageToPlayer(message);

    }

    @Override
    public void parseMessage(String message, PlayerConnectionHandler player)
    {
        mapFunction(message, player);
    }

    @Override
    public void addListener(PlayerConnectionHandler player) {
        synchronized (lock) { players.add(player); }
    }

    @Override
    public void removeListener(PlayerConnectionHandler player) {
        synchronized (lock) { players.remove(player); }
    }

    private void mapFunction(String message, PlayerConnectionHandler player)
    {

        switch (message)
        {
            default:
                try
                {
                    broadcastMessage(message);
                }
                catch (PlayerSendMessageException e)
                {
                    throw new RuntimeException(e);
                }

        }

    }
}

class ServerRunnable implements Runnable
{
    private volatile ServerSocket serverSocket;
    PlayerHandlerCallback onAdd;

    private volatile boolean isRunning = false;

    int PORT = 4321;

    ServerRunnable(PlayerHandlerCallback onAdd)
    {
        this.onAdd = onAdd;

        try
        {
            serverSocket = new ServerSocket(PORT);
        }
        catch (IOException e)
        {
            ServerInterruptException interruptException =  new ServerInterruptException(e.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(interruptException);
        }
    }

    public synchronized void toggleRunning()
    {
        isRunning = !isRunning;
        if(isRunning)
        {
            notify();
            System.out.println("Server Notified");
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run()
    {

        while (true)
        {
            try
            {
                synchronized (this)
                {
                    if (!isRunning)
                    {
                        System.out.println("Server Waiting...");
                        serverSocket.close();
                        wait();
                        serverSocket = new ServerSocket(PORT);
                    }
                }

                System.out.println("Server Accepting...");
                Socket clientSocket = serverSocket.accept();
                if(isRunning)
                {
                    onAdd.call(new PlayerConnectionHandler(clientSocket));
                    System.out.println("Client Connected: " + clientSocket.getInetAddress().getHostAddress());
                }
                else
                {
                    clientSocket.close();
                }
            }
            catch (SocketException | InterruptedException ex)
            {
                System.out.println("Server Closed, Exception: ---> " + ex.getMessage());
            }
            catch (IOException e)
            {
                ServerInterruptException customException = new ServerInterruptException(e.getStackTrace());
                ExceptionHandlerMiddleware.getInstance().handleException(customException);
            }
        }
    }
}
