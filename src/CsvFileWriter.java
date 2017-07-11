import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 3;

    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static String PAGE_HEADER = "";

    static {
        String[] headerLine = {"Название раздела", "GUID идентификатор группы элементов"
                , "GUID идентификатор родительской группы элементов", "Заголовок раздела(title)"
                , "Описание раздела(description)", "Ключевые слова раздела(keywords)"
                , "Описание раздела", "Путь для раздела", "Порядок сортировки раздела"
                , "GUID идентификатор элемента", "Название элемента", "Описание элемента", "Текст для элемента"
                , "Метки", "Активность элемента", "Порядок сортировки элемента", "Путь к элементу"
                , "Заголовок (title)", "Значение мета-тега description для страницы с элементом"
                , "Значение мета-тега keywords для страницы с элементом", "Флаг индексации", "Дата"
                , "Дата публикации", "Дата завершения публикации", "Файл изображения для элемента"
                , "Файл малого изображения для элемента", "Ярлыки", "Идентификатор пользователя сайта"};
        String[] headerTextBox = {"Форматированное текстовое поле", "Чек бокс"};
        String youtube = "Адрес (youtube)";
        String[] headerElements = {"Заголовок", "Ссылка", "Описание"};

        for (String str : headerLine) {
            PAGE_HEADER += "\"" + str +"\"" + DELIMITER;
        }

        for (int i = 1; i < NUMBER_TEXT_BOX; i++) {
            PAGE_HEADER += "\"" + headerTextBox[0] + i + "-1"+ "\"" + DELIMITER +
                            "\"" + headerTextBox[1] + i + "-1" + "\"" + DELIMITER;
        }

        PAGE_HEADER += "\"" + youtube +"\"" + DELIMITER;

        for (int i = 1; i < NUMBER_ELEMENT; i++) {
            PAGE_HEADER += "\"" + headerElements[0] + i + "-1\"" + DELIMITER +
                            "\"" + headerElements[1] + i + "-1\"" + DELIMITER +
                            "\"" + headerElements[2] + i + "-1\"" + DELIMITER;
        }
    }

    public static void write(String fileName, List<Page> pages) {
        FileWriter fileWriter = null;


        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append(PAGE_HEADER.toString());
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

//            for (Page page : pages) {
//                fileWriter.append(page.getNameParagraph());
//                fileWriter.append(DELIMITER);
//                fileWriter.append(NEW_LINE_SEPARATOR);
//            }

            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }

    }

}
