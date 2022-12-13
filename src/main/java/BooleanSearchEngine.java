import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Реализация поискового движка, которую вам предстоит написать. Слово Boolean пришло из теории
 * информационного поиска, тк наш движок будет искать в тексте ровно то слово, которое было указано,
 * без использования синонимов и прочих приёмов нечёткого поиска.
 */
public class BooleanSearchEngine implements SearchEngine {
    private static final Map<String, List<PageEntry>> indexingOfFoundWords = new HashMap<>(); // индекс найденных слов

    public BooleanSearchEngine(File[] files) throws IOException, NullPointerException {
        for (File Extension : Objects.requireNonNull(files)) {
            PdfDocument PDF = new PdfDocument(new PdfReader(Extension));
            for (int countingPage = 1; countingPage < PDF.getNumberOfPages(); countingPage++) { // перебираем страницы текущего PDF файла, страницы PDF файла начинаются всегда с "1"
                String text = PdfTextExtractor.getTextFromPage(PDF.getPage(countingPage)); // извлекаем весь текст текущей страницы
                String[] words = text.split("\\P{IsAlphabetic}+"); // разбить текст на слова (а они в этих документах разделены могут быть не только пробелами)
                Map<String, Integer> hashMapWords = new HashMap<>(); // создаем карту, где храним слова и количество встречи его на странице
                transferTheMapAndCalculateWords(hashMapWords, words);// вызов метода для перемещения слов в структуру карты, вычисляем частоту встречи слов
                writingToTheIndexingOfFoundWords(hashMapWords, Extension, countingPage); // вывоз метода поиска запроса в индексируемой карте и записи результата в массив
            }
        }
    }

    // Метод переноса слов в структуру карты и подсчет количества одинаковых слов без учета регистра (мы хотим регистро-независимый поиск).
    private static void transferTheMapAndCalculateWords(Map<String, Integer> hashMapWords, String[] words) {
        for (String word : words) {
            if (hashMapWords.containsKey(word)) { // если слово обнаружено больше одного раза
                hashMapWords.put(word.toLowerCase(), hashMapWords.get(word) + 1); //Он есть в карте. Значит он нам уже встречался. Вынимаем текущее значение для ключа (нашего слова), увеличиваем это число на 1 и вставляем обратно в карту.
            } else { //если слово обнаружено первый раз
                hashMapWords.put(word, 1); //Его нет в карте. Значит, мы встретили его в первый раз и вставляем в карте пару, где ключ это наше слово, а значение это 1 (тк встретили его всего один раз).
            }
        }
    }

    // Метод записи в карту (индексации) общего результата найденных слов во всех файлах.
    private static void writingToTheIndexingOfFoundWords(Map<String, Integer> hashMapWords, File Extension, int countingPage) {
        for (Map.Entry<String, Integer> entry : hashMapWords.entrySet()) {
            List<PageEntry> wordSearchPageResult; // создаем массив для хранения результата поиска по странично слов
            if (indexingOfFoundWords.containsKey(entry.getKey())) { // проверка на наличие слова в индексе
                wordSearchPageResult = indexingOfFoundWords.get(entry.getKey());
            } else {
                wordSearchPageResult = new ArrayList<>(); // создаем новый массив для вывода результата поиска по каждой странице и числу найденных слов
            }
            wordSearchPageResult.add(new PageEntry(Extension.getName(), countingPage, entry.getValue())); // по порядку добавляем в массив данные, которые описывает класс PageEntry
            indexingOfFoundWords.put(entry.getKey(), wordSearchPageResult); // добавляем результат поиска в индекс в формате ранее созданного массива
        }
    }

    @Override // переопределим поиск по слову
    public List<PageEntry> search(String word) {
        return indexingOfFoundWords.get(word.toLowerCase()); // переводим слово в нижний регистр
    }
}
