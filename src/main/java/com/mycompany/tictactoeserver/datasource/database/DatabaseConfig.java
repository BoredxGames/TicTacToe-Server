/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tictactoeserver.datasource.database;

/**
 *
 * @author mahmoud
 */
public final class DatabaseConfig {

    private DatabaseConfig() {
    }

    public static final String URL =
            "jdbc:mysql://localhost:3306/tictactoe_db";

    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";
}