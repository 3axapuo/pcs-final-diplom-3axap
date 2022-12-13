import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Сейчас в нём находится заготовка использования поискового движка. После его реализации,
 * содержимое main нужно будет заменить на запуск сервера, обслуживающего поисковые запросы
 */
public class Main {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Применение настройки ведения журнала Java с помощью SLF4J");
        // создаем потоки
        new Thread(() -> TCPClient.main(args)).start();
        Thread t1 = new Thread(() -> TCPServer.main(args));
        t1.start(); // старт серверной части
        Thread t2 = new Thread(() -> TCPClient.main(args));
        t2.start(); // старт клиентской части
    }
}