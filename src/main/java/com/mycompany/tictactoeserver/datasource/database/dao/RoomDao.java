/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.datasource.database.dao;
import com.mycompany.tictactoeserver.datasource.database.Database;
import com.mycompany.tictactoeserver.domain.exception.*;
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
String sql =
    "INSERT INTO ROOM (id, first_player_id, second_player_id, status, created_at) " +
    "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement roomCreationStatement = connection.prepareStatement(sql)) {
            roomCreationStatement.setString(1, room.getId());
            roomCreationStatement.setString(2, room.getFirstPlayerId());
            roomCreationStatement.setString(3, room.getSecondPlayerId());
            roomCreationStatement.setInt(4, room.getStatus());
            roomCreationStatement.setTimestamp(5, Timestamp.valueOf(room.getCreatedAt()));
            roomCreationStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new RoomCreationException(ex.getStackTrace());
        }
    }

    public void updateRoomStatus(String roomId, int status) throws RoomUpdationException{
        String sqlUpdate = "UPDATE ROOM SET status = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlUpdate)) {
            ps.setInt(1, status);
            ps.setString(2, roomId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RoomUpdationException(ex.getStackTrace());
        }
    }

    public Room findRoomById(String roomId) throws RoomNotFoundException {
        String sqlSearchingByID = "SELECT * FROM ROOM WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sqlSearchingByID)) {
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

        } catch (SQLException ex) {
            throw new RoomNotFoundException(ex.getStackTrace());
        }
    }
}