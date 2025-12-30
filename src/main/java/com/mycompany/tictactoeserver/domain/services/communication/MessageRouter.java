package com.mycompany.tictactoeserver.domain.services.communication;

import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.authentication.AuthenticationService;
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
                Message msg = handleRequest(json);
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

    private Message handleRequest(JSONObject json) {
        Action action = Action.valueOf(json.getInt("action"));

        assert action != null;
        return switch (action) {
            case LOGIN ->
                    AuthenticationService.getInstance().login(json.getString("username"), json.getString("password"));
            case REGISTER ->
                    AuthenticationService.getInstance().register(json.getString("username"), json.getString("password"));
            default -> {
                System.out.println("Unknown Action: " + action);
                yield new Message();
            }
        };
    }


}
