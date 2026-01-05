package com.mycompany.tictactoeserver.domain.services.playerSession;

import com.mycompany.tictactoeserver.datasource.database.dao.SessionDAO;
import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.domain.utils.exception.*;

import java.util.List;

public class PlayerSessionService {
    private final SessionDAO sessionDao;
    private final ExceptionHandlerMiddleware exceptionHandler;

    public PlayerSessionService() {
        this.sessionDao = new SessionDAO();
        this.exceptionHandler = ExceptionHandlerMiddleware.getInstance();
    }

    public List<Session> getAllPlayerSessions() {
        try {
            return sessionDao.getAllSessions();

        } catch (SessionNotFoundException e) {
            exceptionHandler.handleException(e);
        }
        return List.of();
    }

    public boolean startPlayerSession(String playerId) {
        try {
            Session session = new Session(playerId);
            return sessionDao.startSession(session);

        } catch (ActiveSessionExistsException e) {
            String[] data = {playerId, e.getMessage()};
            exceptionHandler.handleException(e, data);
            return false;
        }
    }

    public boolean endPlayerSession(String playerId) {
        try {
            return sessionDao.endSessionByPlayerId(playerId);

        } catch (SessionNotFoundException e) {
            String[] data = {playerId};
            exceptionHandler.handleException(e, data);
            return false;

        } catch (DataAccessException e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    public boolean isPlayerActive(String playerId) {
        return sessionDao.hasActiveSession(playerId);
    }

    public long getPlayerPlayTime(String playerId) {
        try {
            return sessionDao.getTotalPlayTimeMinutes(playerId);

        } catch (DataAccessException e) {
            exceptionHandler.handleException(e);
            return 0;
        }
    }
}