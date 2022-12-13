/**
 * Класс, описывающий один элемент результата одного поиска. Он состоит из имени пдф-файла,
 * номера страницы и количества раз, которое встретилось это слово на ней
 */
public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName; // имя файла
    private final int page; // страница
    private final int count; // число слов

    PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName; // имя файла
        this.page = page; // страница
        this.count = count; // число слов
    }

    @Override // переопределяем метод вывода в консоль полей в формате JSON
    public String toString() {
        return "PageEntry {pdf = " + pdfName + ", page = " + page + ", count = " + count;
    }

    // Также, списки ответов для каждого слова должны быть отсортированы в порядке уменьшения поля count.
    // Для этого предлагается классу PageEntry сразу реализовывать интерфейс Comparable.
    @Override  // переопределяем метод сравнения объекта по числу в порядке уменьшения поля
    public int compareTo(PageEntry word) {
        if (this.count > word.count) {
            return 1;
        } else if (this.count < word.count) {
            return -1;
        }
        return 0;
    }
}
