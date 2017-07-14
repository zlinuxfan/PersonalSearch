import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter_Page {

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

        for (int i = 1; i <= NUMBER_TEXT_BOX; i++) {
            PAGE_HEADER += "\"" + headerTextBox[0] + i + "-1"+ "\"" + DELIMITER +
                            "\"" + headerTextBox[1] + i + "-1" + "\"" + DELIMITER;
        }

        PAGE_HEADER += "\"" + youtube +"\"" + DELIMITER;

        for (int i = 1; i <= NUMBER_ELEMENT; i++) {
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
            fileWriter.append(PAGE_HEADER);
            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Page page : pages) {
                fileWriter.append("\"").append(page.getNameParagraph()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getGuidOfGroup()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getGuidOfParentGroup()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getSectionTitle()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getSectionDescription()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(arrayToCsvFormat(page.getSectionKeywords())).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getBriefDescriptionSection()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getPathSection()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getPartitionSortingSection()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getGuidOfElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getNameOfElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getBriefDescriptionElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getTextOfElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getTags()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getElementActiviti()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getSortingOrderOfElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getPathlElement()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getElementTitle()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getElementDescription()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getElementKeywords()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.isIndexing() ? "1": "0").append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getData()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getDataOfPublication()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getDataOfPublicationEnd()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getPathImage()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getPathImageSmall()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getShortcuts()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(page.getSiteUserID()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(textBoxesToCsv(page.getTextBoxs())).append("\"");
                fileWriter.append("\"").append(page.getPathYouTube()).append("\"");
                fileWriter.append(DELIMITER);
                fileWriter.append("\"").append(urlInfoListToCsv(page.getUrlInfoList())).append("\"");
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");
        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter_Page !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }

    }

    private static String crateIframe(int width, int height, String src, int frameborder) {
        return String.format(
                "<iframe width=\"%d\" height=\"%d\" src=\"%s\" frameborder=\"%d\" allowfullscreen></iframe>",
                width, height, src, frameborder
        );
    }

    private static String urlInfoListToCsv(List<UrlInfo> urlInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (UrlInfo urlInfo: urlInfoList) {
            if (urlInfo.isYoutube()) {
                continue;
            }
            stringBuilder.append(
                    String.format("\"%s\"%s\"%s\"%s\"%s\"",
                            urlInfo.getHeading(), DELIMITER,
                            urlInfo.getLink(), DELIMITER,
                            urlInfo.getDescription()
                    ))
                    .append(DELIMITER);
            if (counter == NUMBER_ELEMENT) {
                break;
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    private static String textBoxesToCsv(ArrayList<OnceText> textBoxes) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (OnceText onceText: textBoxes) {
            stringBuilder.append(
                    String.format("\"%s\"%s\"%s\"",
                            onceText.getTextBox(), DELIMITER, onceText.isCheckBox() ? "1" : "0"
                    ))
                    .append(DELIMITER);
            if (counter == NUMBER_TEXT_BOX) {
                break;
            }
            counter++;
        }
        return stringBuilder.toString();
    }

    private static String arrayToCsvFormat(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        if (array != null && array.length > 0) {
            for (String str : array) {
                stringBuilder.append(str)
                        .append(" ");
            }
        } else {
            stringBuilder.append("");
        }
        return stringBuilder.toString();
    }
}
