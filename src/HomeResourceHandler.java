import java.util.Map;

public class HomeResourceHandler implements IResourceHandler {

    @Override
    public ResponceContent handle(Map<String, String> params) {
        ResponceContent responceContent = new ResponceContent();
        responceContent.setMimeType("text/html; charset=utf-8");

        // HTML-контент главной страницы
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\"/>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "    <title>Home page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Добро пожаловать на главную страницу!</h1>\n" +
                "<p>Это динамически обработанный контент для URI /home.</p>\n" +
                "</body>\n" +
                "</html>";

        responceContent.setContent(content.getBytes());  // Устанавливаем HTML-контент
        return responceContent;
    }
}
