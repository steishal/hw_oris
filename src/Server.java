import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {
    public static final int SERVER_PORT = 50000;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started on port " + SERVER_PORT);

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // Обработка запроса в отдельном потоке
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                new Thread(requestHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
