/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.datasource.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author sheri
 */
public class DatabaseTest {
    public static void main(String[] args) {
        Database db = Database.getInstance();

        System.out.println("Attempting to connect to the database...");
        db.connect();

        Connection conn = (Connection) db.getConnection();

        if (conn != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection failed.");
        }

        
        try {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Connection is open and ready to use.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking connection status: " + e.getMessage());
        }

        // Disconnect
        db.disconnect();
        System.out.println("Disconnected from database.");
    }
}
