/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.datasource.database.dao;
import com.mycompany.tictactoeserver.domain.exception.RoomCreationException;
import com.mycompany.tictactoeserver.datasource.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 *
 * @author mahmoud 
 */
public class RoomDao {
    

    private final Connection connection;

    public RoomDao() {
        this.connection = Database.getInstance().getConnection();
    }

    public void createRoom(Room room) throws RoomCreationException {
        

        try (
             String createRoom = """
            INSERT INTO ROOM (id, first_player_id, second_player_id, status, created_at)
            VALUES (?, ?, ?, ?, ?)
        """;   
                PreparedStatement ps = connection.prepareStatement(createRoom)) {
            ps.setString(1, room.getId());
            ps.setString(2, room.getFirstPlayerId());
            ps.setString(3, room.getSecondPlayerId());
            ps.setInt(4, room.getStatus());
            ps.setTimestamp(5, Timestamp.valueOf(room.getCreatedAt()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RoomCreationException();
        }
    }

    public void updateRoomStatus(String roomId, int status) {
        String sql = "UPDATE ROOM SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, roomId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update room status", e);
        }
    }

    public Room findRoomById(String roomId) {
        String sql = "SELECT * FROM ROOM WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setFirstPlayerId(rs.getString("first_player_id"));
                room.setSecondPlayerId(rs.getString("second_player_id"));
                room.setWinnerId(rs.getString("winner_id"));
                room.setStatus(rs.getInt("status"));
                return room;
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find room by id", e);
        }
    }
}