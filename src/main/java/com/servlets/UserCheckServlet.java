package com.servlets;

import com.DBConnection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet("/usercheck")
public class UserCheckServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("name");
        String userPassword = request.getParameter("password");

        String sql = "SELECT * FROM users WHERE name = ? AND password_hash = ?";

        try  {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userPassword);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                HttpSession session = request.getSession();
                // Сохраним в сессии пользователя его имя
                session.setAttribute("user", userName);
                response.sendRedirect(getServletContext().getContextPath() + "/index");
            } else {
                response.sendRedirect(getServletContext().getContextPath() + "/login");
            }
            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}







