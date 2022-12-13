import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class TCPServer {
    private static final int PORT = 8989; // Порт для подключения к серверу
    private static final String LOCALDIR = "pdfs"; // Каталог с файлами формата PDF

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // стартуем сервер один(!) раз
            File[] foundFilesToSearch = searchFilesByFormat(LOCALDIR, "pdf");
            BooleanSearchEngine engine = new BooleanSearchEngine(foundFilesToSearch);

            while (true) { // в цикле(!) принимаем подключения
                try (Socket clientSocket = serverSocket.accept(); // ожидаем подключения
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // true, очистка буфера
                     BufferedReader in = new BufferedReader
                             (new InputStreamReader(clientSocket.getInputStream()))) {
                    out.println("СЕРВЕР (порт " + PORT + "): запустился и принимает поисковые запросы в каталоге " + LOCALDIR);
                    List<PageEntry> resultInTheIndex = engine.search(in.readLine()); // возвращаем найденный результат из индекса
                    resultInTheIndex.sort(Collections.reverseOrder()); // сортируем результат поиска в обратном порядке (как на примере задания по частоте найденных слов на странице), перед выводом в консоль
                    out.println(new Gson().toJson(resultInTheIndex)); // передаем клиенту результат поиска в формате JSON
                }
            }
        } catch (IOException e) {
            System.out.println("СЕРВЕР: запуск завершился неудачей.");
            e.printStackTrace();
        }
    }

    // метод для поиска файлов с указанием адреса каталога и расширения файлов
    public static File[] searchFilesByFormat(String directoryAddress, String fileExtension) {
        try {
            File f = new File(directoryAddress);
            FileFilter filter = f1 -> f1.getName().endsWith(fileExtension);
            File[] files = f.listFiles(filter);
            System.out.println("СЕРВЕР: список " + fileExtension + " файлов для текстового поиска:");

            assert files != null;
            for (int file = 0; file < files.length; file++) {
                System.out.println(file + 1 + ". " + files[file].getName()); // выводим на экран список найденных файлов
            }
            return files; // возвращаем список подходящих файлов
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
