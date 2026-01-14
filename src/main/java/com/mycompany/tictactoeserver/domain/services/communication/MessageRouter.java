package com.mycompany.tictactoeserver.domain.services.communication;

import com.google.gson.Gson;
import com.mycompany.tictactoeserver.domain.entity.AuthRequestEntity;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.authentication.AuthenticationService;
import com.mycompany.tictactoeserver.domain.services.game.GameEndInfo;
import com.mycompany.tictactoeserver.domain.services.game.GameManager;
import com.mycompany.tictactoeserver.domain.services.game.GameRequestInfo;
import com.mycompany.tictactoeserver.domain.services.game.GameResponseInfo;
import com.mycompany.tictactoeserver.domain.services.game.MoveInfo;
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
                handleResponse(response, sender);
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
           
             Message response= AuthenticationService.getInstance().login(auth,sender);
             
             String msg = gson.toJson(response);
                sender.sendMessageToPlayer(msg);
                System.out.println(msg);
            }

            case REGISTER -> {
                System.out.println("come reg");
                

                AuthRequestEntity auth = gson.fromJson(message.getData(), AuthRequestEntity.class);

             
               Message response=   AuthenticationService.getInstance().register(auth);
               String msg = gson.toJson(response);
               sender.sendMessageToPlayer(msg);
            }
            case GET_AVAILABLE_PLAYERS -> {
    Message response = server.getAvailablePlayersMessage(sender);
    sender.sendMessageToPlayer(gson.toJson(response));
}
            case REQUEST_GAME -> {
                GameRequestInfo requestInfo = gson.fromJson(message.getData(), GameRequestInfo.class);
                PlayerConnectionHandler target = GameServerManager.getInstance().getPlayerById(requestInfo.getTargetId());
    Message response = GameManager.getInstance().requestGame(requestInfo, sender, target);
            sender.sendMessageToPlayer(gson.toJson(response));

}
            case GET_LEADERBOARD -> {
    Message response = server.getLeaderboardMessage();
    sender.sendMessageToPlayer(gson.toJson(response));
}
             

            default -> {
//                System.out.println("Unknown Action: " + action);
//                yield new Message(new Header(MessageType.ERROR, Action.REGISTER), null);
            }
        };
    }
    private void handleResponse(Message msg, PlayerConnectionHandler sender) throws PlayerSendMessageException {
    Action action = msg.getHeader().getAction();
    Message response;

    switch (action) {

        case GAME_RESPONSE -> {
            
            GameResponseInfo requestInfo = gson.fromJson(msg.getData(), GameResponseInfo.class);
            response = GameManager.getInstance().handleGameResponse(requestInfo, sender);
if (response != null) {
                sender.sendMessageToPlayer(gson.toJson(response));
            }        }

        case SEND_GAME_UPDATE -> {
            MoveInfo moveInfo = gson.fromJson(msg.getData(), MoveInfo.class);
            GameManager.getInstance().forwardMove(moveInfo);
        }
         
        case GAME_END->{
            GameEndInfo Info = gson.fromJson(msg.getData(), GameEndInfo.class);
            GameManager.getInstance().handleGameEnd(Info,sender);
        }

        default -> {
            response = new Message(new Header(MessageType.ERROR, action), "Unknown response action");
            sender.sendMessageToPlayer(gson.toJson(response));
        }
    }

}
}