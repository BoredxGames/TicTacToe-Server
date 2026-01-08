package com.mycompany.tictactoeserver.domain.services.communication;

import com.google.gson.Gson;
import com.mycompany.tictactoeserver.domain.entity.AuthRequestEntity;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.authentication.AuthenticationService;
import com.mycompany.tictactoeserver.domain.utils.exception.PlayerSendMessageException;

public class MessageRouter {

    private static MessageRouter instance;
    private final GameServerManager server;
    private Gson gson = new Gson();

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
        Message response = gson.fromJson(message, Message.class);
        System.out.println("navigate message");
        MessageType messageType = response.getHeader().getMsgType();

        assert messageType != null;
        switch (messageType) {
            case REQUEST:
                handleRequest(response , sender);
                //  server.sendMessage(msg.toJson().toString(), sender);
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

    private void handleRequest(Message message , PlayerConnectionHandler sender) {
        Action action = message.getHeader().getAction();
        Message responseMessage ;
        if (action == null) {
            System.out.println("Action is null");
           responseMessage= new  Message(new Header(MessageType.ERROR, Action.REGISTER), null);
        }

        switch (action) {

            case LOGIN -> {
                System.out.println(message.toString());
                AuthRequestEntity auth = gson.fromJson(message.getData(), AuthRequestEntity.class);

             Message response=   AuthenticationService.getInstance().login(auth);
             String msg = gson.toJson(response);
                sender.sendMessageToPlayer(msg);
                System.out.println(msg);
            }

            case REGISTER -> {
                System.out.println("come reg");

                AuthRequestEntity auth = gson.fromJson(message.getData(), AuthRequestEntity.class);

                AuthenticationService.getInstance().register(auth);

            }

            default -> {
//                System.out.println("Unknown Action: " + action);
//                yield new Message(new Header(MessageType.ERROR, Action.REGISTER), null);
            }
        };
    }

}
