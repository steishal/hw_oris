package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection _instance;
    private static Connection connection;

    public synchronized static DBConnection getInstance() throws SQLException {
        if (_instance == null) {
            _instance = new DBConnection();
        }
        return _instance;
    }

    private DBConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hw6", "anastasia", "201710202814003621");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Ошибка при загрузке драйвера: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static void releaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения с БД: " + e.getMessage());
        }
    }
}
