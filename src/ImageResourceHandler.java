import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ImageResourceHandler implements IResourceHandler {
    @Override
    public ResponceContent handle(Map<String, String> params) {
        ResponceContent responceContent = new ResponceContent();
        responceContent.setMimeType("image/gif");


        try {
            Path imagePath = Path.of("/Users/anastasia/IdeaProjects/hw3/ cat.GIF"); // Путь к вашему изображению
            if (Files.exists(imagePath)) {
                responceContent.setContent(Files.readAllBytes(imagePath));  // Чтение содержимого файла
            } else {
                responceContent = null;  // Если файл не найден, вернем null для 404
            }
        } catch (IOException e) {
            e.printStackTrace();
            responceContent = null;  // Ошибка при чтении файла
        }

        return responceContent;
    }
}