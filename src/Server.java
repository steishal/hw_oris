import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int SERVER_PORT = 50000;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(SERVER_PORT)) {
            System.out.println("start server");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Connected: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // Создаем новый поток для обработки клиента
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}