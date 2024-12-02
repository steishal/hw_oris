package com;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;

import static com.DBConnection.releaseConnection;

@WebListener
public class ContextListener implements ServletContextListener {
    private DBConnection dbConnection;
    public void contextInitialized(ServletContextEvent sce) {
        try {
            dbConnection = DBConnection.getInstance();
            System.out.println("Соединение с БД установлено.");
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании соединения с базой данных:", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        // Закрываем подключение
        if(dbConnection != null){
            releaseConnection((Connection) dbConnection);
            System.out.println("Соединение с БД закрыто.");
        } else{
            System.out.println("Соединение с БД не было установлено.");
        }
    }
}




