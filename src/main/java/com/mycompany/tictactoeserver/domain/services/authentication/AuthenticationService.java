package com.mycompany.tictactoeserver.domain.service.authentication;

import com.mycompany.tictactoeserver.datasource.database.dao.PlayerDAO;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.communication.Header;
import com.mycompany.tictactoeserver.domain.communication.Message;
import com.mycompany.tictactoeserver.domain.communication.MessageType;
import com.mycompany.tictactoeserver.domain.entity.AuthResponseEntity;
import com.mycompany.tictactoeserver.domain.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.exception.HashingException;
import com.mycompany.tictactoeserver.domain.exception.ServerInterruptException;
import com.mycompany.tictactoeserver.domain.security.ServerSecurityManager;
import org.json.JSONObject;

public class AuthenticationService {


    private static AuthenticationService instance;
    private final PlayerDAO playerDao;

    private AuthenticationService() {
        this.playerDao = new PlayerDAO();
    }

    public static AuthenticationService getInstance() {
        if (instance == null)
            instance = new AuthenticationService();

        return instance;
    }


    public Message register(String username, String plainTextPassword) {

        Player player = playerDao.findByUsername(username);

        if (player != null) {
            JSONObject json = new JSONObject();
            json.put("error", "Username already exists");
            return new Message(new Header(MessageType.ERROR, Action.REGISTER), json);
        }

        AuthResponseEntity responseEntity;

        try {

            String hashedPassword = ServerSecurityManager.hashText(plainTextPassword);
            Player newPlayer = new Player(username, hashedPassword);
            playerDao.insert(newPlayer);
            responseEntity = new AuthResponseEntity(newPlayer);

        } catch (HashingException ex) {

            ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.REGISTER), json);

        }


        return new Message(new Header(MessageType.RESPONSE, Action.REGISTER), responseEntity.toJson());

    }

    public Message login(String username, String plainTextPassword) {

        try {

            Player player = playerDao.findByUsername(username);

            if (player == null) {
                JSONObject json = new JSONObject();
                json.put("error", "Invalid Credentials");
                return new Message(new Header(MessageType.ERROR, Action.LOGIN), json);
            }


            String hashedPassword = ServerSecurityManager.hashText(plainTextPassword);
            if (!hashedPassword.equals(player.getPassword())) {
                JSONObject json = new JSONObject();
                json.put("error", "Invalid Credentials");
                return new Message(new Header(MessageType.ERROR, Action.LOGIN), json);
            }


            AuthResponseEntity responseEntity = new AuthResponseEntity(player);
            return new Message(new Header(MessageType.RESPONSE, Action.LOGIN), responseEntity.toJson());

        } catch (HashingException ex) {
            ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            JSONObject json = new JSONObject();
            json.put("error", "Internal Server Error");
            return new Message(new Header(MessageType.ERROR, Action.LOGIN), json);
        }
    }


}
