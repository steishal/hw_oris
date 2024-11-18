import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HomeResourceHandler implements IResourceHandler {

    @Override
    public ResponceContent handle(Map<String, String> params, String someOtherParam) {
        ResponceContent responceContent = new ResponceContent();
        responceContent.setMimeType("text/html; charset=utf-8");

        // Декодируем параметры
        String name = decodeParam(params.getOrDefault("name", "Гость"));
        String message = decodeParam(params.getOrDefault("message", "Добро пожаловать на сайт!"));

        // HTML-контент главной страницы
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "    <title>Home page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Добрый день, " + name + "!</h1>\n" +
                "<p>" + message + "</p>\n" +
                "</body>\n" +
                "</html>";

        // Устанавливаем HTML-контент с кодировкой UTF-8
        responceContent.setContent(content.getBytes(StandardCharsets.UTF_8));
        return responceContent;
    }

    // Метод для декодирования параметров
    private String decodeParam(String param) {
        try {
            return URLDecoder.decode(param, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return param; // Если ошибка декодирования, возвращаем исходное значение
        }
    }
}




