
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponceContent {
    private String mimeType;  // MIME-тип контента (например, "text/html" или "image/png")
    private byte[] content;   // Содержимое ответа (например, HTML-код, изображение и т.д.)
}


