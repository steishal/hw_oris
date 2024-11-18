import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Map<String, IResourceHandler> resources;

    public ClientHandler(Socket clientSocket, Map<String, IResourceHandler> resources) {
        this.clientSocket = clientSocket;
        this.resources = resources;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isEmpty()) {
                return;
            }

            String[] firstLine = headerLine.split("\\s+");
            String method = firstLine[0];
            String uriWithParams = firstLine[1];
            System.out.println("Request: " + method + " " + uriWithParams);

            Map<String, String> paramMap = new HashMap<>();
            String[] uriParts = uriWithParams.split("\\?");
            String uri = uriParts[0];

            if (uriParts.length > 1) {
                for (String param : uriParts[1].split("&")) {
                    String[] keyValue = param.split("=");
                    paramMap.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                }
            }

            IResourceHandler handler = resources.get(uri);
            ResponceContent responseContent;
            if (handler != null) {
                responseContent = handler.handle(paramMap, uri);
                writeResponse(outputStream, "200 OK", responseContent);
            } else {
                writeResponse(outputStream, "404 Not Found", null);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(OutputStream outputStream, String status, ResponceContent responseContent) throws IOException {
        String headers = "HTTP/1.1 " + status + "\r\n" +
                "Server: Server\r\n" +
                (responseContent != null
                        ? "Content-Type: " + responseContent.getMimeType() + "\r\n" +
                        "Content-Length: " + responseContent.getContent().length + "\r\n"
                        : "") +
                "\r\n";

        outputStream.write(headers.getBytes());
        if (responseContent != null) {
            outputStream.write(responseContent.getContent());
        }
        outputStream.flush();
    }
}

