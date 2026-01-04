package com.mycompany.tictactoeserver.domain.services.communication;

import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.authentication.AuthenticationService;
import com.mycompany.tictactoeserver.domain.services.game.GameManager;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerSendMessageException;
import org.json.JSONObject;

public class MessageRouter {
    private static MessageRouter instance;
    private final GameServerManager server;

    private MessageRouter() {
        server = GameServerManager.getInstance();
    }

    public static MessageRouter getInstance() {
        if (instance == null) {
            instance = new MessageRouter();
        }
        return instance;
    }

    public void navigateMessage(String message, PlayerConnectionHandler sender) throws PlayerSendMessageException {
        JSONObject json = new JSONObject(message);

        MessageType messageType = MessageType.valueOf(json.getInt("type"));

        assert messageType != null;
        switch (messageType) {
            case REQUEST:
                Message msg = handleRequest(json,sender);
                server.sendMessage(msg.toJson().toString(), sender);
                break;
            case RESPONSE:
                // Handle response messages
                break;
            case EVENT:
                // Handle event messages
                break;
            case ERROR:
                // Handle error messages
                break;
            default:
                System.out.println("Unknown MessageType: " + messageType);
                break;
        }
    }

    private Message handleRequest(JSONObject json,PlayerConnectionHandler sender) {
        Action action = Action.valueOf(json.getInt("action"));

        assert action != null;
        return switch (action) {
            case LOGIN ->
                    AuthenticationService.getInstance().login(json.getString("username"), json.getString("password"));
            case REGISTER ->
                    AuthenticationService.getInstance().register(json.getString("username"), json.getString("password"));
           case REQUEST_GAME -> {
    PlayerConnectionHandler target =
            server.getPlayerById(json.getString("targetId"));
        yield GameManager.getInstance().requestGame(sender, target);      
}
            case GAME_RESPONSE -> {
            boolean accepted = json.getBoolean("accepted");
            yield GameManager.getInstance().handleGameResponse(sender, accepted);
        }
        case SEND_GAME_UPDATE -> {
            String roomId = json.getString("roomId");
            JSONObject move = json.getJSONObject("move");
            yield GameManager.getInstance().forwardMove(roomId, sender, move);
        }
           

            default -> {
                System.out.println("Unknown Action: " + action);
                yield new Message();
            }
        };
    }


}
