package com.mycompany.tictactoeserver.domain.services.game;
import com.google.gson.Gson;
import com.mycompany.tictactoeserver.domain.entity.PlayerStatus;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.communication.*;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.ServerInterruptException;

import java.util.Vector;

public class GameManager {

    private static GameManager instance;
    private final Vector<GameRoom> activeRooms = new Vector<>();
    private final Vector<GameRequest> pendingRequests = new Vector<>();
    private final Object lock = new Object();
    private Gson gson = new Gson();


    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    private boolean isPlayerUnavailable(PlayerConnectionHandler player) {
        return player.getStatus() == PlayerStatus.PENDING || player.getStatus() == PlayerStatus.IN_GAME;
    }

    private boolean hasOutgoingPending(PlayerConnectionHandler requester) {
        synchronized (lock) {
            for (GameRequest req : pendingRequests) {
                if (req.requester == requester) return true;
            }
        }
        return false;
    }

    private GameRequest findPendingByTarget(PlayerConnectionHandler target) {
        synchronized (lock) {
            for (GameRequest req : pendingRequests) {
                if (req.target == target) return req;
            }
        }
        return null;
    }
    
    private GameRoom getRoomById(String roomId) {
        synchronized (lock) {
            for (GameRoom room : activeRooms) {
                if (room.getRoomId().equals(roomId)) return room;
            }
        }
        return null;
    }

    private PlayerConnectionHandler getPlayerById(String playerId) {
        for (GameRoom room : activeRooms) {
            if (room.getPlayer1().getPlayer().getId().equals(playerId)) return room.getPlayer1();
            if (room.getPlayer2().getPlayer().getId().equals(playerId)) return room.getPlayer2();
        }
        return null;
    }


    public Message requestGame(GameRequestInfo requestInfo, PlayerConnectionHandler requester, PlayerConnectionHandler target) {
        try {
            if (requester == null || target == null) {
                return Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, requestInfo);
            }

            if (isPlayerUnavailable(requester) || isPlayerUnavailable(target)) {
                return Message.createMessage(MessageType.ERROR, Action.PLAYER_BUSY, requestInfo);
            }

            if (hasOutgoingPending(requester)) {
                return Message.createMessage(MessageType.ERROR, Action.PENDING_REQUEST_EXISTS, requestInfo);
            }

            if (findPendingByTarget(target) != null) {
                return Message.createMessage(MessageType.ERROR, Action.TARGET_HAS_PENDING_REQUEST, requestInfo);
            }

     
            requester.setStatus(PlayerStatus.PENDING);
            target.setStatus(PlayerStatus.PENDING);
            GameServerManager.getInstance().broadcastPlayerList();


            synchronized (lock) {
                pendingRequests.add(new GameRequest(requester, target));
            }
            Message eventToTarget = Message.createMessage( MessageType.EVENT, Action.REQUEST_GAME, requestInfo);
target.sendMessageToPlayer(gson.toJson(eventToTarget));

 
            return Message.createMessage(MessageType.RESPONSE, Action.REQUEST_GAME, requestInfo);

        } catch (Exception ex) {
            ExceptionHandlerMiddleware.getInstance()
                    .handleException(new ServerInterruptException(ex.getStackTrace()));
            return Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, requestInfo);
        }
    }

    public Message handleGameResponse(GameResponseInfo requestInfo, PlayerConnectionHandler responder) {
        try {
            GameRequest found = null;

            synchronized (lock) {
                for (GameRequest req : pendingRequests) {
                    if (req.target == responder) {
                        found = req;
                        break;
                    }
                }
                if (found != null) pendingRequests.remove(found);
            }

            if (found == null) {
                return Message.createMessage(MessageType.ERROR, Action.NO_PENDING_REQUEST, requestInfo);
            }

            PlayerConnectionHandler requester = found.requester;

            if (!requestInfo.isAccepted()) {
                requester.setStatus(PlayerStatus.ONLINE);
                responder.setStatus(PlayerStatus.ONLINE);
                return Message.createMessage(MessageType.RESPONSE, Action.GAME_RESPONSE, requestInfo);
            }

            return startGame(requester, responder);

        } catch (Exception ex) {
            ExceptionHandlerMiddleware.getInstance()
                    .handleException(new ServerInterruptException(ex.getStackTrace()));
            return Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, requestInfo);
        }
    }

    private Message startGame(PlayerConnectionHandler p1, PlayerConnectionHandler p2) {
        try {
            GameRoom room = new GameRoom(p1, p2);

            p1.setStatus(PlayerStatus.IN_GAME);
            p2.setStatus(PlayerStatus.IN_GAME);
            GameServerManager.getInstance().broadcastPlayerList();
            synchronized (lock) {
                activeRooms.add(room);
            }

            GameStartInfo startInfo = new GameStartInfo(
                    room.getRoomId(),
                    p1.getPlayer().getId(),
                    p2.getPlayer().getId()
            );

            notifyPlayersGameStarted(room);

            return Message.createMessage(MessageType.RESPONSE, Action.GAME_START, startInfo);

        } catch (Exception ex) {
            ExceptionHandlerMiddleware.getInstance()
                    .handleException(new ServerInterruptException(ex.getStackTrace()));
            return Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, null);
        }
    }

    public void notifyPlayersGameStarted(GameRoom room) {
        try {
            GameStartInfo startInfo = new GameStartInfo(
                    room.getRoomId(),
                    room.getPlayer1().getPlayer().getId(),
                    room.getPlayer2().getPlayer().getId()
            );

            Message msg = Message.createMessage(MessageType.EVENT, Action.GAME_START, startInfo);

            room.getPlayer1().sendMessageToPlayer(gson.toJson(msg));
            room.getPlayer2().sendMessageToPlayer(gson.toJson(msg));

        } catch (Exception e) {
            ExceptionHandlerMiddleware.getInstance()
                    .handleException(new ServerInterruptException(e.getStackTrace()));
        }
    }


    public Message forwardMove(MoveInfo moveInfo) {
        try {
            GameRoom room = getRoomById(moveInfo.getRoomId());
            if (room == null) {
                return Message.createMessage(MessageType.ERROR, Action.ROOM_NOT_FOUND, moveInfo);
            }

            PlayerConnectionHandler sender = getPlayerById(moveInfo.getPlayerId());
            PlayerConnectionHandler opponent = room.getOpponent(sender);
            if (opponent == null) {
                return Message.createMessage(MessageType.ERROR, Action.INVALID_OPPONENT, moveInfo);
            }

            Message msg = Message.createMessage(MessageType.RESPONSE, Action.SEND_GAME_UPDATE, moveInfo);
            opponent.sendMessageToPlayer(gson.toJson(msg));

            return Message.createMessage(MessageType.RESPONSE, Action.SEND_GAME_UPDATE, moveInfo);

        } catch (Exception ex) {
            ExceptionHandlerMiddleware.getInstance()
                    .handleException(new ServerInterruptException(ex.getStackTrace()));
            return Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, moveInfo);
        }
    }

 
   
}
