import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    public static final int PORT = 8989; // Порт для подключения к серверу
    public static final String HOST = "localhost"; // имя (localhost) или IP (127.0.0.1) - адрес для подключения к серверу

    public static void main(String[] args) {
        while (true) { // в цикле(!) подключаемся к серверу
            try (Socket clientSocket = new Socket(HOST, PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) // true, очистка буфера
            {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
                String serverLine = in.readLine(); // читаем (получаем) приглашение от сервера
                System.out.println(serverLine + "\n" + "КЛИЕНТ (порт " + PORT + "): какое слово будем искать?"); // выводим в консоль приглашение от сервера и запрашиваем строку для поиска
                String clientLine = buffer.readLine(); // получаем вводные данные с консоли
                out.println(clientLine); // отсылаем вводные данные (запрос) серверу
                String serverJson = in.readLine(); // читаем (получаем) ответ от сервера в формате JSON
                System.out.println(serverJson); // выводим в консоль результат поиска сформированный сервером

            } catch (IOException e) {
                System.out.println("КЛИЕНТ: запуск завершился неудачей.");
                throw new RuntimeException(e);
            }
        }
    }
}