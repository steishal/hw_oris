package com.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@WebServlet("*.thtml")
public class TemplateHandlerServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String servletPath = request.getServletPath();

        // Загружаем шаблон
        InputStream inputStream = TemplateHandlerServlet.class.getClassLoader().getResourceAsStream(servletPath);

        if (inputStream == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            try {
                response.getWriter().write("Template not found: " + servletPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        try {
            byte[] content = inputStream.readAllBytes();
            String contentTemplate = new String(content);

            // Заменяем параметры в шаблоне
            String processedContent = replaceTemplateParameters(contentTemplate, request);

            // Выводим результат
            response.setContentType("text/html");
            response.getWriter().write(processedContent);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String replaceTemplateParameters(String template, HttpServletRequest request) {
        // Ищем параметры вида ${param}
        StringBuilder result = new StringBuilder();
        int pos = 0;

        while (true) {
            int start = template.indexOf("${", pos);
            if (start == -1) {
                result.append(template.substring(pos));
                break;
            }

            int end = template.indexOf("}", start);
            if (end == -1) {
                result.append(template.substring(pos));
                break;
            }

            result.append(template.substring(pos, start));

            String paramName = template.substring(start + 2, end);

            // Сначала ищем значение в атрибутах, потом в параметрах запроса
            String value = (String) request.getAttribute(paramName);
            if (value == null) {
                value = request.getParameter(paramName);
            }

            result.append(value != null ? value : ""); // Если значения нет, оставляем пустую строку

            pos = end + 1;
        }

        return result.toString();
    }
}
