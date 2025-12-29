package com.mycompany.tictactoeserver.datasource.database;

import com.mycompany.tictactoeserver.domain.exception.DatabaeDisConnectionException;
import com.mycompany.tictactoeserver.domain.exception.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance = null;
    private Connection connection = null;

    private Database() { }

    public static Database getInstance() {
        
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        
        return instance;
    }

    public void connect() throws DatabaseConnectionException{
        if (connection != null) {
            return;
        }

        try {
            connection = DriverManager.getConnection(
                    DatabaseConfig.URL,
                    DatabaseConfig.USERNAME,
                    DatabaseConfig.PASSWORD
            );
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getStackTrace());
        }
    }

    public void disconnect() throws DatabaeDisConnectionException{
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
           throw new DatabaeDisConnectionException(e.getStackTrace());
        } finally {
            connection = null;
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
