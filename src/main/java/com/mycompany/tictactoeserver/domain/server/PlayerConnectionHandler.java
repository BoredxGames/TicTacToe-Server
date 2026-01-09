package com.mycompany.tictactoeserver.domain.server;

import com.google.gson.Gson;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.AuthRequestEntity;
import com.mycompany.tictactoeserver.domain.entity.PlayerStatus;
import com.mycompany.tictactoeserver.domain.services.communication.Message;
import com.mycompany.tictactoeserver.domain.utils.callbacks.StringCallback;
import com.mycompany.tictactoeserver.domain.utils.callbacks.VoidCallback;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerConnectionException;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerSendMessageException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerConnectionHandler {

    private static final GameServerManager serverManager = GameServerManager.getInstance();
    private Player player;
    private final PlayerRunnable runnable;
    private PlayerStatus status = PlayerStatus.OFFLINE;


    PlayerConnectionHandler(Socket socket) {
        runnable = new PlayerRunnable(socket, this::receiveMessageFromPlayer, this::onCloseRunnable);

        Thread thread = new Thread(runnable);

        thread.start();
    }

    public void sendMessageToPlayer(String message) {
        try {
            runnable.sendMessageToSocket(message);
        } catch (PlayerSendMessageException e) {
            System.out.println("Excetion sending message");
        }
    }

    public void receiveMessageFromPlayer(String message) {
        serverManager.onReceiveMessage(message, this);
    }

    public void close() {
        runnable.setRunning(false);
        runnable.close();
         setStatus(PlayerStatus.OFFLINE);
    }

    private void onCloseRunnable() {
        serverManager.removePlayer(this);
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
        System.out.println("Player " + (player != null ? player.getUsername() : "unknown") + " status: " + status);
    }
}


class PlayerRunnable implements Runnable {
    private final Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    StringCallback onReceive;
    VoidCallback onClose;

    private volatile boolean isRunning = true;

    PlayerRunnable(Socket socket, StringCallback onReceive, VoidCallback onClose) {
        this.onReceive = onReceive;
        this.onClose = onClose;
        this.socket = socket;

        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            close();
            PlayerConnectionException connectionException = new PlayerConnectionException(e.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(connectionException);

        }
    }

    public synchronized void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        if (this.isRunning) {
            notify();
            System.out.println("Notified");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    if (!isRunning) {
                        System.out.println("Player Waiting...");
                        wait();
                    }
                }
                System.out.println("Player Reading...");
                String msg = in.readUTF();

                System.out.println("in: ---> " + msg);
                onReceive.call(msg);
            }
        } catch (InterruptedException | IOException ex) {
            onClose.call();
            close();
            System.out.println("Client Disconnected");
        }
    }

    public void sendMessageToSocket(String message) throws PlayerSendMessageException {
        try {
            System.out.println("out: ---> " + message);
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            throw new PlayerSendMessageException(e.getStackTrace());
        }
    }

    public void close() {
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {
        }

        try {
            if (out != null) out.close();
        } catch (IOException ignored) {
        }

        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {
        }
    }
}