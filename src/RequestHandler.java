import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class RequestHandler implements Runnable {
    private final Socket clientSocket;

    public RequestHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Request: " + requestLine);
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String uri = requestParts[1];

            if (method.equals("GET")) {
                handleGetRequest(out, uri);
            } else if (method.equals("POST")) {
                handlePostRequest(in, out);
            } else {
                sendResponse(out, "HTTP/1.1 405 Method Not Allowed", "Method Not Allowed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGetRequest(OutputStream out, String uri) throws IOException {
        // Путь к файлу index.html
        Path filePath = Path.of("/Users/anastasia/IdeaProjects/hw2", uri.equals("/") ? "index.html" : uri.substring(1));

        if (Files.exists(filePath)) {
            byte[] fileContent = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            sendResponse(out, "HTTP/1.1 200 OK", fileContent, contentType);
        } else {
            sendResponse(out, "HTTP/1.1 404 Not Found", "File Not Found");
        }
    }

    private void handlePostRequest(BufferedReader in, OutputStream out) throws IOException {
        String line;
        int contentLength = 0;

        // Чтение заголовков
        while (!(line = in.readLine()).isEmpty()) {
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }

        char[] body = new char[contentLength];
        in.read(body, 0, contentLength);
        String requestBody = new String(body);

        System.out.println("POST Body: " + requestBody);
        sendResponse(out, "HTTP/1.1 200 OK", "POST request received");
    }

    private void sendResponse(OutputStream out, String statusLine, String body) throws IOException {
        byte[] bodyBytes = body.getBytes();
        String headers = "Content-Type: text/htmlrn; charset=utf-8\r\n" +
                "Content-Length: " + bodyBytes.length + "\r\n\r\n";

        out.write((statusLine + "\r\n" + headers).getBytes());
        out.write(bodyBytes);
        out.flush();
    }

    private void sendResponse(OutputStream out, String statusLine, byte[] body, String contentType) throws IOException {
        String headers = "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.length + "\r\n\r\n";

        out.write((statusLine + "\r\n" + headers).getBytes());
        out.write(body);
        out.flush();
    }
}



