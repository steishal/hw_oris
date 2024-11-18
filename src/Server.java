import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static final int SERVER_PORT = 40000;

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(SERVER_PORT);
            System.out.println("Server started on port " + SERVER_PORT);

            Map<String, IResourceHandler> resources = new HashMap<>();
            resources.put("/dynamic", new ImageResourceHandler());
            resources.put("/home", new HomeResourceHandler());

            while (true) {
                Socket clientSocket = server.accept();
                Thread clientThread = new Thread(new ClientHandler(clientSocket, resources));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}







