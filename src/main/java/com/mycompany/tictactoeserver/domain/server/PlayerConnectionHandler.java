package com.mycompany.tictactoeserver.domain.server;

import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.exception.PlayerConnectionException;
import com.mycompany.tictactoeserver.domain.exception.PlayerReceiveMessageException;
import com.mycompany.tictactoeserver.domain.exception.PlayerSendMessageException;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerConnectionHandler extends Thread {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Player player;
    private static final GameServerManager serverManager = GameServerManager.getInstance();

    PlayerConnectionHandler(Socket socket) throws PlayerConnectionException {

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            start();
        } catch (IOException e) {
            throw new PlayerConnectionException(e.getStackTrace());
        }
    }

    @Override
    public void run() {
        try {
            listen();
        } catch (PlayerReceiveMessageException e) {
            ExceptionHandlerMiddleware.getInstance().handleException(e);
        }
    }

    private void listen() throws  PlayerReceiveMessageException {

        try {
            while (true) {
                String msg = in.readUTF();
                serverManager.parseMessage(msg);
            }
        } catch (IOException ex) {
            serverManager.removeListener(this);
            throw new PlayerReceiveMessageException(ex.getStackTrace());
        }
    }

    public void sendMessage(String message) throws PlayerSendMessageException {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new PlayerSendMessageException(e.getStackTrace());
        }
    }

    public void close() {
        try {
            interrupt();
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            in = null;
            out = null;
            socket = null;
        }

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }



}