import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    public static Date current = new Date();
    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String message;
            while ((message = bufferedReader.readLine()) != null) {
                System.out.println("From client " + clientSocket.getPort() + ":" + message);

                // Ответ в зависимости от полученного сообщения
                switch (message) {
                    case "Привет":
                        bufferedWriter.write("Привет\n");
                        break;
                    case "Exit":
                        bufferedWriter.write("Вы завершили общение\n");
                        bufferedWriter.flush();
                        return;
                    case "Какая сегодня погода?":
                        bufferedWriter.write("Погода сегодня замечательная\n");
                        break;
                    case "Как дела?":
                        bufferedWriter.write("Отлично\n");
                        break;
                    case "Какое сегодня число?":
                        bufferedWriter.write(String.valueOf(current.getDate()) + "\n");
                        break;
                    case "Который час?":
                        bufferedWriter.write(String.valueOf(current.getHours()) + ":" + String.valueOf(current.getMinutes()) + "\n");
                        break;
                    default:
                        bufferedWriter.write("Задайте другой вопрос\n");
                        break;
                }
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
