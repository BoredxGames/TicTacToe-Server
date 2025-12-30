package com.mycompany.tictactoeserver.datasource.database.dao;

import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.datasource.model.ActivityPoint;
import com.mycompany.tictactoeserver.domain.exception.ActiveSessionExistsException;
import com.mycompany.tictactoeserver.domain.exception.ActivityNotFoundException;
import com.mycompany.tictactoeserver.domain.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tasneem
 */
public class ActivityDao {
    private final Connection connection;

    public ActivityDao() {
        this.connection = Database.getInstance().getConnection();
    }

    public boolean startActivity(ActivityPoint activity) throws ActiveSessionExistsException {
        ActivityPoint existingSession = getActiveSessionByPlayerId(activity.getPlayerId());
        if (existingSession != null) {
            String[] exceptionData = {activity.getPlayerId(), existingSession.getId()};
            throw new ActiveSessionExistsException(exceptionData);
        }

        String sql = "INSERT INTO ACTIVITY (id, player_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, activity.getId());
            preparedStatement.setString(2, activity.getPlayerId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(activity.getStartTime()));
            preparedStatement.setTimestamp(4, activity.getEndTime() != null ? Timestamp.valueOf(activity.getEndTime()) : null);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error starting activity: " + e.getMessage());
            return false;
        }
    }

    public boolean endActivity(ActivityPoint activity) throws ActivityNotFoundException, DataAccessException {
        String sql = "UPDATE ACTIVITY SET end_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(activity.getEndTime()));
            preparedStatement.setString(2, activity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ActivityNotFoundException(activity.getId());
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public boolean endActivityByPlayerId(String playerId) throws ActivityNotFoundException, DataAccessException {
        ActivityPoint activeSession = getActiveSessionByPlayerId(playerId);
        if (activeSession == null) {
            throw new ActivityNotFoundException("No active session for player: " + playerId);
        }

        String sql = "UPDATE ACTIVITY SET end_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, activeSession.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public ActivityPoint getActivityById(String activityId) throws ActivityNotFoundException, DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, activityId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToActivity(resultSet);
                }
                throw new ActivityNotFoundException(activityId);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public List<ActivityPoint> getActivitiesByPlayerId(String playerId) throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE player_id = ? ORDER BY start_date DESC";
        List<ActivityPoint> activities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    activities.add(mapResultSetToActivity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return activities;
    }

    public ActivityPoint getActiveSessionByPlayerId(String playerId) {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE player_id = ? AND end_date IS NULL ORDER BY start_date DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToActivity(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting active session: " + e.getMessage());
        }
        return null;
    }

    public List<ActivityPoint> getActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE start_date BETWEEN ? AND ? ORDER BY start_date DESC";
        List<ActivityPoint> activities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    activities.add(mapResultSetToActivity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return activities;
    }

    public int getActivityCountByPlayerId(String playerId) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM ACTIVITY WHERE player_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return 0;
    }

    public boolean hasActiveSession(String playerId) {
        return getActiveSessionByPlayerId(playerId) != null;
    }

    public boolean deleteActivity(String activityId) throws ActivityNotFoundException, DataAccessException {
        String sql = "DELETE FROM ACTIVITY WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, activityId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new ActivityNotFoundException(activityId);
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public int deleteActivitiesByPlayerId(String playerId) throws DataAccessException {
        String sql = "DELETE FROM ACTIVITY WHERE player_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public List<ActivityPoint> getAllActiveSessions() throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE end_date IS NULL ORDER BY start_date DESC";
        List<ActivityPoint> activities = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    activities.add(mapResultSetToActivity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return activities;
    }

    public long getTotalPlayTimeMinutes(String playerId) throws DataAccessException {
        String sql = "SELECT start_date, end_date FROM ACTIVITY WHERE player_id = ? AND end_date IS NOT NULL";
        long totalMinutes = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Timestamp start = resultSet.getTimestamp("start_date");
                    Timestamp end = resultSet.getTimestamp("end_date");

                    if (start != null && end != null) {
                        long durationMillis = end.getTime() - start.getTime();
                        totalMinutes += durationMillis / (1000 * 60);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return totalMinutes;
    }

    private ActivityPoint mapResultSetToActivity(ResultSet resultSet) throws SQLException {
        ActivityPoint activity = new ActivityPoint();
        activity.setPlayerId(resultSet.getString("player_id"));

        Timestamp startTimestamp = resultSet.getTimestamp("start_date");
        if (startTimestamp != null) {
            activity.setStartTime(startTimestamp.toLocalDateTime());
        }

        Timestamp endTimestamp = resultSet.getTimestamp("end_date");
        if (endTimestamp != null) {
            activity.setEndTime(endTimestamp.toLocalDateTime());
        }

        return activity;
    }
}