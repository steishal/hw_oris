package com.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class IndexPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Устанавливаем параметры
        request.setAttribute("param1", "Hello, World!");
        request.setAttribute("param2", "This is a test parameter.");

        try {
            // Перенаправляем на шаблон index.thtml
            request.getRequestDispatcher("/template/index.thtml").forward(request, response);
        } catch (ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
