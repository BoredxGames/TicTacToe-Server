package com.mycompany.tictactoeserver.domain.services.authentication;

import com.mycompany.tictactoeserver.datasource.database.dao.PlayerDAO;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.entity.AuthRequestEntity;
import com.mycompany.tictactoeserver.domain.entity.AuthResponseEntity;
import com.mycompany.tictactoeserver.domain.entity.PlayerStatus;
import com.mycompany.tictactoeserver.domain.server.GameServerManager;
import com.mycompany.tictactoeserver.domain.server.PlayerConnectionHandler;
import com.mycompany.tictactoeserver.domain.services.communication.Action;
import com.mycompany.tictactoeserver.domain.services.communication.Message;
import com.mycompany.tictactoeserver.domain.services.communication.MessageType;
import com.mycompany.tictactoeserver.domain.services.playerSession.PlayerSessionService;
import com.mycompany.tictactoeserver.domain.services.security.ServerSecurityManager;
import com.mycompany.tictactoeserver.domain.utils.exception.ExceptionHandlerMiddleware;
import com.mycompany.tictactoeserver.domain.utils.exception.HashingException;
import com.mycompany.tictactoeserver.domain.utils.exception.ServerInterruptException;
import java.sql.SQLException;

/**
 *
 * @author Hazem
 *
 */
public class AuthenticationService {

    private static AuthenticationService instance;
    private final PlayerDAO playerDao;
    private final PlayerSessionService playerSessionService;

    private AuthenticationService() {
        this.playerDao = new PlayerDAO();
        this.playerSessionService = new PlayerSessionService();
    }

    public static AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public Message register(AuthRequestEntity credential) {
        Message response;

        System.out.println(credential.getUserName() + "  " + credential.getPassword());
        if (credential.getUserName() == null || credential.getPassword() == null) {
            response = Message.createMessage(MessageType.ERROR, Action.USERNAME_NOT_FOUND, credential);
        }

        try {

            Player player = playerDao.findByUsername(credential.getUserName());

            if (player != null) {
                System.out.println("ظياض");
                response = Message.createMessage(MessageType.ERROR, Action.USERNAME_ALREADY_EXIST, credential);
                return response;
            }

            String hashedPassword = ServerSecurityManager.hashText(credential.getPassword());
            Player newPlayer = new Player(credential.getUserName(), hashedPassword);
            playerDao.insert(newPlayer);

            AuthResponseEntity responseEntity = new AuthResponseEntity(newPlayer);
            response = Message.createMessage(MessageType.RESPONSE, Action.REGISTERATION_SUCCESS, responseEntity);

        } catch (HashingException ex) {
            ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            response = Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, credential);
        } catch (SQLException ex) {
             response = Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, credential);
                return response;
        }
        return response;
    }

    public Message login(AuthRequestEntity credential, PlayerConnectionHandler clientSession) {
        Message response;
        if (credential == null || credential.getUserName() == null || credential.getPassword() == null) {
            response = Message.createMessage(MessageType.ERROR, Action.USERNAME_NOT_FOUND, credential);
        }

        try {
            Player player = playerDao.findByUsername(credential.getUserName());

            if (player == null) {

                response = Message.createMessage(MessageType.ERROR, Action.USERNAME_NOT_FOUND, credential);
                return response;
            }

            String hashedPassword = ServerSecurityManager.hashText(credential.getPassword());
            if (!hashedPassword.equals(player.getPassword())) {

                response = Message.createMessage(MessageType.ERROR, Action.INVALID_CREDENTIAL, credential);
                return response;
            }
            if (GameServerManager.getInstance().isPlayerOnline(credential.getUserName())) {
                response = Message.createMessage(MessageType.ERROR, Action.USER_IS_ONLINE, credential);
                return response;
            }
            playerSessionService.startPlayerSession(player.getId());

            AuthResponseEntity responseEntity = new AuthResponseEntity(player);
            response = Message.createMessage(MessageType.RESPONSE, Action.LOGIN_SUCCESS, responseEntity);
            if (clientSession != null) {
                clientSession.setPlayer(player);
                clientSession.setStatus(PlayerStatus.ONLINE);
                GameServerManager.getInstance().broadcastPlayerList();
                GameServerManager.getInstance().runCallbacks();
            }
        } catch (HashingException ex) {
            ServerInterruptException customException = new ServerInterruptException(ex.getStackTrace());
            ExceptionHandlerMiddleware.getInstance().handleException(customException);

            response = Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, credential);

        } catch (SQLException ex) {
            response = Message.createMessage(MessageType.ERROR, Action.INTERNAL_SERVER_ERROR, credential);
            return response;
        }
        return response;
    }
}
