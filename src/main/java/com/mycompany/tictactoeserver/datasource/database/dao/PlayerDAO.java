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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class PlayerDAO {
       private final Connection connection;

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
            throw new RuntimeException("Error inserting player", e);
        }
    }

    public Player findById(String id) {
        String sql = "SELECT * FROM player WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player by ID", e);
        }
        return null;
    }

    public Player findByUsername(String username) {
        String sql = "SELECT * FROM player WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding player by username", e);
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
            throw new RuntimeException("Error fetching all players", e);
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
            throw new RuntimeException("Error updating player", e);
        }
    }

    public void delete(String id) {
        String sql = "DELETE FROM player WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting player", e);
        }
    }

    private Player mapRow(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("score")
        );
    }
}
