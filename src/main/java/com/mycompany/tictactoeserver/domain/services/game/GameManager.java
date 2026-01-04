package com.mycompany.tictactoeserver.domain.services.game;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.communication.*;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.ServerInterruptException;
import org.json.JSONObject;
import java.util.Vector;

public class GameManager {
    private static GameManager instance;
    private final Vector<GameRoom> activeRooms = new Vector<>();
    private final Vector<GameRequest> pendingRequests = new Vector<>();
    private final Object lock = new Object();
    private GameManager() {}
    public static GameManager getInstance() {
        if (instance == null)
            instance = new GameManager();
        return instance;
    }
    
    private boolean isPlayerInGame(PlayerConnectionHandler player) {
        Vector<GameRoom> copy;
        synchronized (lock) {
            copy = new Vector<>(activeRooms);
        }
        for (GameRoom room : copy) {
            if (room.hasPlayer(player)) return true;
        }
        return false;
    }

    private boolean hasOutgoingPending(PlayerConnectionHandler requester) {
        Vector<GameRequest> copy;
        synchronized (lock) {
            copy = new Vector<>(pendingRequests);
        }
        for (GameRequest req : copy) {
            if (req.requester == requester) return true;
        }
        return false;
    }

    private GameRequest findPendingByTarget(PlayerConnectionHandler target) {
        Vector<GameRequest> copy;
        synchronized (lock) {
            copy = new Vector<>(pendingRequests);
        }
        for (GameRequest req : copy) {
            if (req.target == target) return req;
        }
        return null;
    }

    public Message requestGame(PlayerConnectionHandler requester,
                               PlayerConnectionHandler target) {

        try {
            if (requester == null || target == null) {
                JSONObject json = new JSONObject();
                json.put("error", "Invalid players");
                return new Message(new Header(MessageType.ERROR, Action.REQUEST_GAME), json);
            }

            if (isPlayerInGame(requester) || isPlayerInGame(target)) {
                JSONObject json = new JSONObject();
                json.put("error", "Player already in game");
                return new Message(new Header(MessageType.ERROR, Action.REQUEST_GAME), json);
            }

            if (hasOutgoingPending(requester)) {
                JSONObject json = new JSONObject();
                json.put("error", "Already waiting for response");
                return new Message(new Header(MessageType.ERROR, Action.REQUEST_GAME), json);
            }

            if (findPendingByTarget(target) != null) {
                JSONObject json = new JSONObject();
                json.put("error", "Target already has pending request");
                return new Message(new Header(MessageType.ERROR, Action.REQUEST_GAME), json);
            }

            synchronized (lock) {
                pendingRequests.add(new GameRequest(requester, target));
            }

            JSONObject data = new JSONObject();
            data.put("requesterId", requester.getPlayer().getId());

            return new Message(
                    new Header(MessageType.REQUEST, Action.REQUEST_GAME),
                    data
            );

        } catch (Exception ex) {
            ServerInterruptException customException =
                    new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.REQUEST_GAME), json);
        }
    }

    public Message handleGameResponse(PlayerConnectionHandler responder,
                                      boolean accepted) {

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
                JSONObject json = new JSONObject();
                json.put("error", "No pending request");
                return new Message(new Header(MessageType.ERROR, Action.GAME_RESPONSE), json);
            }

            if (!accepted) {
                JSONObject data = new JSONObject();
                data.put("message", "Game request rejected");

                return new Message(
                        new Header(MessageType.REQUEST, Action.GAME_RESPONSE),
                        data
                );
            }

            return startGame(found.requester, responder);

        } catch (Exception ex) {
            ServerInterruptException customException =
                    new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.GAME_RESPONSE), json);
        }
    }

    private Message startGame(PlayerConnectionHandler p1,
                              PlayerConnectionHandler p2) {

        try {
            GameRoom room = new GameRoom(p1, p2);

            synchronized (lock) {
                activeRooms.add(room);
            }

            JSONObject data = new JSONObject();
            data.put("roomId", room.getRoomId());
            data.put("player1", p1.getPlayer().getId());
            data.put("player2", p2.getPlayer().getId());

            return new Message(
                    new Header(MessageType.REQUEST, Action.GAME_START),
                    data
            );

        } catch (Exception ex) {
            ServerInterruptException customException =
                    new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.GAME_START), json);
        }
    }

    public Message forwardMove(String roomId,
                               PlayerConnectionHandler sender,
                               JSONObject move) {

        try {
            Vector<GameRoom> copy;
            synchronized (lock) {
                copy = new Vector<>(activeRooms);
            }

            for (GameRoom room : copy) {
                if (room.getRoomId().equals(roomId)) {
                    PlayerConnectionHandler opponent = room.getOpponent(sender);
                    if (opponent == null) {
                        JSONObject json = new JSONObject();
                        json.put("error", "Invalid opponent");
                        return new Message(new Header(MessageType.ERROR, Action.SEND_GAME_UPDATE), json);
                    }

                    return new Message(
                            new Header(MessageType.EVENT, Action.SEND_GAME_UPDATE),
                            move
                    );
                }
            }

            JSONObject json = new JSONObject();
            json.put("error", "Room not found");
            return new Message(new Header(MessageType.ERROR, Action.SEND_GAME_UPDATE), json);

        } catch (Exception ex) {
            ServerInterruptException customException =
                    new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.SEND_GAME_UPDATE), json);
        }
    }
}
