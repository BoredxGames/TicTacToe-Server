package com.mycompany.tictactoeserver.datasource.database.dao;

import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.datasource.model.Session;
import com.mycompany.tictactoeserver.domain.utils.exception.ActiveSessionExistsException;
import com.mycompany.tictactoeserver.domain.utils.exception.SessionNotFoundException;
import com.mycompany.tictactoeserver.domain.utils.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tasneem
 */
public class SessionDAO {
    private final Connection connection;

    public SessionDAO() {
        this.connection = Database.getInstance().getConnection();
    }

    public void startSession(Session session) throws ActiveSessionExistsException {
        Session existingSession = getActiveSessionByPlayerId(session.getPlayerId());
        if (existingSession != null) {
            throw new ActiveSessionExistsException();
        }

        String sql = "INSERT INTO ACTIVITY (id, player_id, start_date, end_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, session.getId());
            preparedStatement.setString(2, session.getPlayerId());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));
            preparedStatement.setTimestamp(4, session.getEndTime() != null ? Timestamp.valueOf(session.getEndTime()) : null);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error starting session: " + e.getMessage());
        }
    }

    public boolean endSession(Session session) throws SessionNotFoundException, DataAccessException {
        String sql = "UPDATE ACTIVITY SET end_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(session.getEndTime()));
            preparedStatement.setString(2, session.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SessionNotFoundException();
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public void endSessionByPlayerId(String playerId) throws SessionNotFoundException, DataAccessException {
        Session activeSession = getActiveSessionByPlayerId(playerId);
        if (activeSession == null) {
            throw new SessionNotFoundException();
        }

        String sql = "UPDATE ACTIVITY SET end_date = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, activeSession.getId());

            int rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public Session getSessionById(String sessionId) throws SessionNotFoundException, DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sessionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSession(resultSet);
                }
                throw new SessionNotFoundException();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public List<Session> getSessionByPlayerId(String playerId) throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE player_id = ? ORDER BY start_date DESC";
        List<Session> sessions = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapResultSetToSession(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return sessions;
    }

    public Session getActiveSessionByPlayerId(String playerId) {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE player_id = ? AND end_date IS NULL ORDER BY start_date DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSession(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting active session: " + e.getMessage());
        }
        return null;
    }

    public List<Session> getSessionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE start_date BETWEEN ? AND ? ORDER BY start_date DESC";
        List<Session> sessions = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapResultSetToSession(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return sessions;
    }

    public int getSessionCountByPlayerId(String playerId) throws DataAccessException {
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

    public boolean deleteSession(String sessionId) throws SessionNotFoundException, DataAccessException {
        String sql = "DELETE FROM ACTIVITY WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, sessionId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SessionNotFoundException();
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public int deletePlayerSessions(String playerId) throws DataAccessException {
        String sql = "DELETE FROM ACTIVITY WHERE player_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, playerId);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
    }

    public List<Session> getAllSessions() throws SessionNotFoundException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY ORDER BY start_date DESC";
        List<Session> sessions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapResultSetToSession(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SessionNotFoundException(e.getStackTrace());
        }
        return sessions;
    }

    public List<Session> getAllActiveSessions() throws DataAccessException {
        String sql = "SELECT id, player_id, start_date, end_date FROM ACTIVITY WHERE end_date IS NULL ORDER BY start_date DESC";
        List<Session> sessions = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(mapResultSetToSession(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getStackTrace());
        }
        return sessions;
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

    private Session mapResultSetToSession(ResultSet resultSet) throws SQLException {
        Session session = new Session();
        session.setPlayerId(resultSet.getString("player_id"));

        Timestamp startTimestamp = resultSet.getTimestamp("start_date");
        if (startTimestamp != null) {
            session.setStartTime(startTimestamp.toLocalDateTime());
        }

        Timestamp endTimestamp = resultSet.getTimestamp("end_date");
        if (endTimestamp != null) {
            session.setEndTime(endTimestamp.toLocalDateTime());
        }

        return session;
    }
}