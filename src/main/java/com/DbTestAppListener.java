package com;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DbTestAppListener implements ServletContextListener {
    private DBConnection dbConnection;

    public void contextInitialized(ServletContextEvent sce) {
        try {
            dbConnection = DBConnection.getInstance();
            System.out.println("Соединение с БД установлено.");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании соединения с базой данных:", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (dbConnection != null) {
            DBConnection.releaseConnection();
            System.out.println("Соединение с БД закрыто.");
        }
    }
}
