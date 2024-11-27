package com;

import com.model.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("")
public class IndexPageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html; charset=UTF-8");

        try {
            // Подключение к базе данных
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Создание списка пользователей
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("id"), resultSet.getString("name")));
            }

            // Закрытие ресурсов
            preparedStatement.close();
            resultSet.close();

            // Настройка FreeMarker
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
            cfg.setDirectoryForTemplateLoading(new File(getServletContext().getRealPath("/WEB-INF/template")));
            cfg.setDefaultEncoding("UTF-8");

            // Создание модели и добавление данных
            Map<String, Object> model = new HashMap<>();
            model.put("users", users);
            model.put("title_page", "Пользователи БД");

            // Получение шаблона и рендеринг
            Template template = cfg.getTemplate("index.ftl");

            // Запись результата в response
            Writer out = response.getWriter();
            template.process(model, out);
        } catch (SQLException | IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
