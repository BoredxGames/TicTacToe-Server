package com.mycompany.tictactoeserver.domain.server;

import com.google.gson.Gson;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.PlayerEntity;
import com.mycompany.tictactoeserver.domain.entity.PlayerStatus;
import com.mycompany.tictactoeserver.domain.services.communication.*;
import com.mycompany.tictactoeserver.domain.services.playerSession.PlayerSessionService;
import com.mycompany.tictactoeserver.domain.services.communication.MessageRouter;
import com.mycompany.tictactoeserver.domain.services.game.GameManager;
import com.mycompany.tictactoeserver.domain.services.statistics.StatisticsService;
import com.mycompany.tictactoeserver.domain.utils.callbacks.PlayerHandlerCallback;
import com.mycompany.tictactoeserver.domain.utils.callbacks.VoidCallback;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerSendMessageException;
import com.mycompany.tictactoeserver.domain.utils.exception.ServerInterruptException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
public class GameServerManager {

    private final Vector<PlayerConnectionHandler> players = new Vector<>();
    private final Vector<VoidCallback> counterListeners = new Vector<>();
    public Thread thread;
    private final ServerRunnable runnable;
    private final Gson gson = new Gson();
    private final Object lock = new Object();
    private final PlayerSessionService playerSessionService;

    private static GameServerManager instance;

    public static GameServerManager getInstance() {
        if (instance == null) {
            instance = new GameServerManager();
        }
        return instance;
    }

    private GameServerManager() {
        this.playerSessionService = new PlayerSessionService();
        runnable = new ServerRunnable(this::addPlayer);
        thread = new Thread(runnable);

        thread.start();
    }

    public boolean isRunning() {
        return runnable.isRunning();
    }

    public PlayerConnectionHandler getPlayerById(String playerId) {
        synchronized (lock) {
            for (PlayerConnectionHandler player : players) {
                if (player.getPlayer() != null
                        &&player.getPlayer().getId().equals(playerId)) {
                    return player;
                }
            }
        }
        return null;
    }
    public Vector<Player> getAvailablePlayerData() {
        Vector<Player> available = new Vector<>();
        synchronized (lock) {
            for (PlayerConnectionHandler handler : players) {
                if (handler.getStatus() == PlayerStatus.ONLINE && handler.getPlayer() != null) {
                    available.add(handler.getPlayer());
                }
            }
        }
        return available;
    }

    public Message getLeaderboardMessage() {
        StatisticsService statsService = new StatisticsService();
    
        List<PlayerEntity> topPlayersList = statsService.getLeaderboard();
    
    Vector<PlayerEntity> leaderboardVector = new Vector<>(topPlayersList);

    AvailablePlayersInfo info = new AvailablePlayersInfo(leaderboardVector, null, null);
    
    return Message.createMessage(MessageType.RESPONSE, Action.GET_LEADERBOARD, info);
}

public void broadcastLeaderboard() {
    Message msg = getLeaderboardMessage();
    String jsonMsg = gson.toJson(msg);
    
    synchronized (lock) {
        for (PlayerConnectionHandler player : players) {
            try {
                if (player.getPlayer() != null) { 
                    player.sendMessageToPlayer(jsonMsg);
                }
            } catch (Exception e) {
                System.out.println("Error broadcasting leaderboard: " + e.getMessage());
            }
        }
    }
}

public Message getAvailablePlayersMessage(PlayerConnectionHandler requester) {
    Vector<PlayerEntity> online = new Vector<>();
    Vector<PlayerEntity> inGame = new Vector<>();
    Vector<PlayerEntity> pending = new Vector<>();

        synchronized (lock) {
            for (PlayerConnectionHandler handler : players) {
                if (handler.getPlayer() == null) {
                    continue;
                }

                if (requester.getPlayer() != null
                        &&handler.getPlayer().getId().equals(requester.getPlayer().getId())) {
                    continue;
                }
                PlayerEntity player = new PlayerEntity(handler.getPlayer().getId(), handler.getPlayer().getUsername(), handler.getPlayer().getScore());

                switch (handler.getStatus()) {
                    case ONLINE ->
                        online.add(player);
                    case IN_GAME ->
                        inGame.add(player);
                    case PENDING ->
                        pending.add(player);
                }
            }
        }

        AvailablePlayersInfo info = new AvailablePlayersInfo(online, inGame, pending);
        return Message.createMessage(MessageType.RESPONSE, Action.GET_AVAILABLE_PLAYERS, info);

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

    public void terminate(){
        stop();
        thread.interrupt();
    }

    public void broadcastPlayerList() {
        synchronized (lock) {
            for (PlayerConnectionHandler player : players) {
                if (player.getPlayer() != null) {
                    Message msg = getAvailablePlayersMessage(player);
                    try {
                        player.sendMessageToPlayer(gson.toJson(msg));
                    } catch (Exception e) {
                        ServerInterruptException interruptException = new ServerInterruptException(e.getStackTrace());
                        ExceptionHandlerMiddleware.getInstance().handleException(interruptException);
                    }
                }
            }
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
            runCallbacks();
        }
    }

    public void removePlayer(PlayerConnectionHandler player) {
        GameManager.getInstance().handlePlayerDisconnect(player);
        synchronized (lock) {
            
            if(player.getPlayer() != null)
            {
                System.out.println("Player is not nulll-----------------------------------------------");
                String playerID = player.getPlayer().getId();
                playerSessionService.endPlayerSession(playerID);
            }
            players.remove(player);
            runCallbacks();
        }
        broadcastPlayerList();
    }

    public int getOnlinePlayersCount() {
        synchronized (lock) {
            int counter = 0;
            for (PlayerConnectionHandler player : players) {
                if (player.getStatus() != PlayerStatus.OFFLINE) {
                    counter++;
                }
            }

            return counter;
        }
    }

    public int getAvailablePlayersCount() {
        synchronized (lock) {
            int counter = 0;
            for (PlayerConnectionHandler player : players) {
                if (player.getStatus() == PlayerStatus.ONLINE) {
                    counter++;
                }
            }
            System.out.println("==============================================================================" + counter);
            return counter;
        }
    }

    public int getInGamePlayersCount() {
        synchronized (lock) {
            int counter = 0;
            for (PlayerConnectionHandler player : players) {
                if (player.getStatus() == PlayerStatus.IN_GAME) {
                    counter++;
                }
            }

            return counter;
        }
    }

    public int getConnectedPlayersCount() {
        return players.size();
    }

    public void addPlayerCountListener(VoidCallback callback) {
        counterListeners.add(callback);
    }

    public void runCallbacks() {
        for (VoidCallback listener : counterListeners) {
            listener.call();
        }
    }

    public boolean isPlayerOnline(String username) {
        synchronized (lock) {
            for (PlayerConnectionHandler player : players) {

                if (player.getPlayer()!=null&&username.equals(player.getPlayer().getUsername())) {
                    return true;
                }
            }
        }
        return false;
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
