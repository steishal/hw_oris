import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final int SERVER_PORT = 40000;
    public static final String ROOT_DIRECTORY = "html/";

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on port " + SERVER_PORT);

            while (true) {  // Цикл для обработки нескольких клиентов
                try (Socket clientSocket = server.accept()) {
                    InputStream inputStream = clientSocket.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    // Чтение первой строки запроса
                    String headerLine = bufferedReader.readLine();
                    System.out.println("Received header line: " + headerLine);
                    if (headerLine == null || headerLine.trim().isEmpty()) {
                        System.out.println("Error: empty or invalid request line.");
                        continue;  // Пропуск этого запроса и ожидание нового
                    }

                    // Разделяем строку на метод, URI и версию HTTP
                    String[] requestParts = headerLine.split(" ");
                    if (requestParts.length != 3) {
                        System.out.println("Error: invalid request line.");
                        continue;  // Пропуск этого запроса и ожидание нового
                    }

                    String method = requestParts[0];
                    String uri = requestParts[1];
                    String httpVersion = requestParts[2];

                    System.out.println("Method: " + method + ", URI: " + uri + ", HTTP Version: " + httpVersion);

                    // Чтение других заголовков запроса (если есть)
                    while ((headerLine = bufferedReader.readLine()) != null && !headerLine.trim().isEmpty()) {
                        System.out.println(headerLine);
                    }

                    Map<String,String> paramMap = new HashMap<>();
                    String[] uriWithParam = uri.split("\\?");
                    if (uriWithParam.length >= 1) {
                        String uri_ = uriWithParam[0];
                        String params = uriWithParam[1];
                        String[] param = params.split("&");
                        for (String s : param) {
                            paramMap.put(s.split("=")[0], s.split("=")[1]);
                            System.out.println(paramMap);
                        }
                    }


                    Map<String, IResourceHandler> resources = new HashMap<>();
                    resources.put("/home", new HomeResourceHandler());
                    resources.put("/cat.GIF", new ImageResourceHandler());

                    // Обработка запроса: если обработчик найден, вызываем его, иначе пытаемся загрузить файл
                    ResponceContent responceContent = null;
                    IResourceHandler handler = resources.get(uri);
                    if (handler != null) {
                        responceContent = handler.handle(paramMap);  // Обработка запроса через ресурсный обработчик
                    } else {
                        responceContent = loadFile(uri);  // Попытка загрузить файл
                    }

                    // Отправка ответа клиенту
                    if (responceContent != null) {
                        // Формирование и отправка заголовков ответа
                        String[] response = {
                                "HTTP/1.1 200 OK\r\n",
                                "Server: NewSuperServer\r\n",
                                "Content-Type: " + responceContent.getMimeType() + "\r\n",
                                "Content-Length: " + responceContent.getContent().length + "\r\n",
                                "\r\n"
                        };

                        for (String responseHeaderLine : response) {
                            clientSocket.getOutputStream().write(responseHeaderLine.getBytes());
                            clientSocket.getOutputStream().flush();
                        }

                        // Отправка содержимого
                        clientSocket.getOutputStream().write(responceContent.getContent());
                        clientSocket.getOutputStream().flush();
                    } else {
                        // Ответ 404, если файл не найден
                        String response = "HTTP/1.1 404 Not Found\r\n\r\n";
                        clientSocket.getOutputStream().write(response.getBytes());
                        clientSocket.getOutputStream().flush();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для загрузки файлов из директории
    private static ResponceContent loadFile(String uri) {
        try {
            Path filePath = Path.of(ROOT_DIRECTORY, uri);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                ResponceContent responceContent = new ResponceContent();
                responceContent.setContent(Files.readAllBytes(filePath)); // Загрузка файла в байты
                responceContent.setMimeType(getMimeType(filePath.toString())); // Установка MIME-типа
                return responceContent;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для определения MIME-типа файла по расширению
    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream"; // MIME-тип по умолчанию
    }
}






