/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.datasource.database.dao;

/**
 *
 * @author sheri
 */

import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.datasource.model.Player;
import com.mycompany.tictactoeserver.domain.utils.exception.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    private final Connection connection;
    private final ExceptionHandlerMiddleware exceptionHandler = ExceptionHandlerMiddleware.getInstance();

    public PlayerDAO() {
        this.connection = Database.getInstance().getConnection();
    }

    public void insert(Player player) {
        String sql = "INSERT INTO player (id, username, password, score) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getId());
            stmt.setString(2, player.getUsername());
            stmt.setString(3, player.getPassword());
            stmt.setInt(4, player.getScore());
            stmt.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerInsertionException(e.getStackTrace()));
        }
    }

    public Player findById(String id) {
        String sql = "SELECT * FROM player WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
            else {
                exceptionHandler.handleException(new PlayerNotFoundException(new StackTraceElement[0]));
            }
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerNotFoundException(e.getStackTrace()));
        }
        return null;
    }

    public Player findByUsername(String username) {
        String sql = "SELECT * FROM player WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerNotFoundException(e.getStackTrace()));
        }
        return null;
    }

    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM player";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) players.add(mapRow(rs));
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerNotFoundException(e.getStackTrace()));
        }
        return players;
    }

    public void update(Player player) {
        String sql = "UPDATE player SET username = ?, password = ?, score = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getUsername());
            stmt.setString(2, player.getPassword());
            stmt.setInt(3, player.getScore());
            stmt.setString(4, player.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerUpdateException(e.getStackTrace()));
        }
    }

    public void delete(String id) {
        String sql = "DELETE FROM player WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            exceptionHandler.handleException(new PlayerDeletionException(e.getStackTrace()));
        }
    }

    private Player mapRow(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("score")
        );
    }
}
