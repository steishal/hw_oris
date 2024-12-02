package com;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Создаем карту для хранения данных аутентификации
        Map<UUID, Long> authData = new HashMap<>();
        sce.getServletContext().setAttribute("AUTH_DATA", authData);
    }
}




