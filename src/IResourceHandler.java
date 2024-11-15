import java.util.Map;

public interface IResourceHandler {
    ResponceContent handle(Map<String, String> params);  // Метод для обработки запроса и формирования ответа
}

