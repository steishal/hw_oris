import java.util.HashMap;
import java.util.Map;

public class ImageResourceHandler implements IResourceHandler {
    private final Map<String, String> mimeTypes;

    public ImageResourceHandler() {
        mimeTypes = new HashMap<>();
        mimeTypes.put("jpg", "image/jpeg");
        mimeTypes.put("html", "text/html; charset=utf-8");
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "application/javascript");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("gif", "image/gif");
    }

    @Override
    public ResponceContent handle(Map<String, String> params, String uri) {
        String ext = uri.substring(uri.lastIndexOf(".") + 1);
        String mimeType = mimeTypes.getOrDefault(ext, "application/octet-stream");

        ResponceContent responseContent = new ResponceContent();
        responseContent.setMimeType(mimeType);

        StringBuilder content = new StringBuilder("<html><body><h1>Dynamic Content</h1><ul>");
        params.forEach((key, value) -> content.append("<li>").append(key).append(": ").append(value).append("</li>"));
        content.append("</ul></body></html>");

        responseContent.setContent(content.toString().getBytes());
        return responseContent;
    }
}