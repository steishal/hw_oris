package com.servlets;

import com.DBConnection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet("/usercheck")
public class UserCheckServlet extends HttpServlet {

    private static final String SECRET_KEY_NAME = "SECRET_KEY";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("login");
        String userPassword = request.getParameter("password");

        String sql = "SELECT * FROM users WHERE name = ? AND password_hash = ?";

        // Получаем соединение из контекста приложения, если оно есть
        Connection connection = (Connection) getServletContext().getAttribute("DB_CONNECTION");

        // Если соединение отсутствует, создаем новое и сохраняем его в контексте
        if (connection == null) {
            try {
                connection = DBConnection.getInstance().getConnection();
                getServletContext().setAttribute("DB_CONNECTION", connection);
            } catch (SQLException e) {
                throw new ServletException("Ошибка при установлении соединения с базой данных", e);
            }
        }

        // Используем try-with-resources для правильного закрытия результата и statement
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // Подготовка параметров для запроса
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);

            // Выполнение запроса
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Если пользователь найден, создаем уникальный идентификатор
                    UUID uuid = UUID.randomUUID();

                    // Получаем объект данных аутентификации из контекста
                    Map<UUID, Long> authData = (Map<UUID, Long>) request.getServletContext().getAttribute("AUTH_DATA");
                    if (authData == null) {
                        authData = new HashMap<>();
                        request.getServletContext().setAttribute("AUTH_DATA", authData);
                    }
                    authData.put(uuid, resultSet.getLong("id"));

                    // Устанавливаем куки с SECRET_KEY
                    Cookie cookie = new Cookie(SECRET_KEY_NAME, uuid.toString());
                    cookie.setMaxAge(60 * 60 * 24);  // Например, куки действуют 24 часа
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);

                    // Переход на страницу после входа
                    response.sendRedirect(request.getContextPath() + "/index");
                } else {
                    // Если пользователь не найден, перенаправляем на страницу входа
                    response.sendRedirect(request.getContextPath() + "/login?error=true");
                }
            }

        } catch (SQLException e) {
            // Логируем ошибку при работе с БД
            throw new ServletException("Ошибка при выполнении запроса к БД", e);
        } finally {
        }
    }
}







