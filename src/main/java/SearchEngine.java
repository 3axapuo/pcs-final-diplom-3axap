import java.util.List;

/**
 * Интерфейс, описывающий поисковый движок. Всё что должен уметь делать поисковый движок,
 * это на запрос из слова отвечать списком элементов результата ответа
 */
public interface SearchEngine {
    List<PageEntry> search(String word);
}
