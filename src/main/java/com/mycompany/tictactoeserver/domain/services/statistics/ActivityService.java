package com.mycompany.tictactoeserver.domain.services.statistics;

import com.mycompany.tictactoeserver.datasource.database.dao.*;
import com.mycompany.tictactoeserver.datasource.model.ActivityPoint;
import com.mycompany.tictactoeserver.domain.utils.exception.*;

public class ActivityService {
    private final ActivityDAO activityDao;
    private final ExceptionHandlerMiddleware exceptionHandler;

    public ActivityService() {
        this.activityDao = new ActivityDAO();
        this.exceptionHandler = ExceptionHandlerMiddleware.getInstance();
    }

    public boolean startPlayerActivity(String playerId) {
        try {
            ActivityPoint activity = new ActivityPoint(playerId);
            return activityDao.startActivity(activity);

        } catch (ActiveSessionExistsException e) {
            String[] data = {playerId, e.getMessage()};
            exceptionHandler.handleException(e, data);
            return false;
        }
    }

    public boolean endPlayerActivity(String playerId) {
        try {
            return activityDao.endActivityByPlayerId(playerId);

        } catch (ActivityNotFoundException e) {
            String[] data = {playerId};
            exceptionHandler.handleException(e, data);
            return false;

        } catch (DataAccessException e) {
            exceptionHandler.handleException(e);
            return false;
        }
    }

    public boolean isPlayerActive(String playerId) {
        return activityDao.hasActiveSession(playerId);
    }

    public long getPlayerPlayTime(String playerId) {
        try {
            return activityDao.getTotalPlayTimeMinutes(playerId);

        } catch (DataAccessException e) {
            exceptionHandler.handleException(e);
            return 0;
        }
    }
}