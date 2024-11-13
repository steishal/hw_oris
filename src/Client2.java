import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client2 {

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("127.0.0.1", 50000);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8))) {

            System.out.println("Connected to server");

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Введите сообщение: ");
                String text = scanner.nextLine();

                // Отправляем сообщение на сервер
                bufferedWriter.write(text + "\n");
                bufferedWriter.flush();

                // Получаем ответ от сервера
                String response = bufferedReader.readLine();
                System.out.println("From server: " + response);

                // Проверяем, хочет ли клиент завершить общение
                if ("Вы завершили общение".equals(response)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
