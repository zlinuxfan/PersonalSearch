import com.ps.Page.OnceText;
import com.ps.Page.Page;
import com.ps.Page.UrlInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CsvFileReader_Page {
    private static final String DELIMITER = ";";
    private static final String SOURCE = "file.csv";
    private static final int NUMBER_URL_PAGE = 5;


    // attributes index
    private static final int nameParagraph = 0;
    private static final int guidOfGroup = 1;
    private static final int guidOfParentGroup = 2;
    private static final int sectionTitle = 3; // Заголовок раздела(title)
    private static final int sectionDescription = 4; // Описание раздела(description)
    private static final int sectionKeywords = 5; // Ключевые слова раздела(keywords)
    private static final int briefDescriptionSection = 6; //Описание раздела
    private static final int pathSection = 7; // Путь для раздела
    private static final int partitionSortingSection = 8; // Порядок сортировки раздела

    private static final int guidOfElement = 9; // GUID идентификатор элемента
    private static final int nameOfElement = 10; // название элемента
    private static final int briefDescriptionElement = 11; //Описание элемента
    private static final int textOfElement = 12; // Текст для элемента
    private static final int tags = 13; // Метки
    private static final int elementActiviti = 14; // Активность элемента
    private static final int sortingOrderOfElement = 15; // Порядок сортировки элемента
    private static final int pathlElement = 16; // Путь к элементу
    private static final int elementTitle = 17; // Заголовок (title)
    private static final int elementDescription = 18; // Значение мета-тега description для страницы с элементом
    private static final int elementKeywords = 19; // Значение мета-тега keywords для страницы с элементом
    private static final int indexing = 20; // Флаг индексации
    private static final int data = 21;
    private static final int dataOfPublication = 22;
    private static final int dataOfPublicationEnd = 23; // Дата завершения публикации;
    private static final int pathImage = 25; // Файл изображения для элемента
    private static final int pathImageSmall = 26; // Файл изображения для элемента
    private static final int shortcuts = 27; // Ярлыки
    private static final int siteUserID = 28; // Идентификатор пользователя сайта

    //ArrayList<com.ps.Page.OnceText> textBoxes
    private static final int textBox1 = 29;
    private static final int textBox2 = 30;
    private static final int textBox2_check = 31;
    private static final int textBox1_check = 32;
    private static final int pathYouTube = 32;

    //List<com.ps.Page.UrlInfo> urlInfo's;
    private static final int urlInfo_title1 = 33;
    private static final int urlInfo_link1 = 34;
    private static final int urlInfo_description1 = 35;
    private static final int urlInfo_title2 = 36;
    private static final int urlInfo_link2 = 37;
    private static final int urlInfo_description2 = 38;
    private static final int urlInfo_title3 = 39;
    private static final int urlInfo_link3 = 40;
    private static final int urlInfo_description3 = 41;
    private static final int urlInfo_title4 = 42;
    private static final int urlInfo_link4 = 43;
    private static final int urlInfo_description4 = 44;
    private static final int urlInfo_title5 = 45;
    private static final int urlInfo_link5 = 46;
    private static final int urlInfo_description5 = 47;

    //ArrayList<com.ps.Page.OnceText> textBoxes tail
    private static final int textBox3 = 48;
    private static final int textBox3_check = 49;

    public static ArrayList<Page> readCsvFile(String fileName) {

        BufferedReader fileReader = null;
        ArrayList<Page> resource = new ArrayList<>();

        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(DELIMITER, 50);

                if (tokens.length > 0) {

                    resource.add(new Page.Builder(
                                    tokens[guidOfElement],
                                    tokens[nameOfElement],
                                    tokens[briefDescriptionElement],
                                    tokens[textOfElement],
                                    tokens[pathlElement],
                                    getOnceTexts(tokens),
                                    getUrlInfos(tokens),
                                    NUMBER_URL_PAGE
                            ).nameParagraph(tokens[nameParagraph])
                                    .guidOfGroup(tokens[guidOfGroup])
                                    .guidOfParentGroup(tokens[guidOfParentGroup])
                                    .sectionTitle(tokens[sectionTitle])
                                    .sectionDescription(tokens[sectionDescription])
                                    .sectionKeywords(tokens[sectionKeywords])
                                    .briefDescriptionSection(tokens[briefDescriptionSection])
                                    .pathSection(tokens[pathSection])
                                    .partitionSortingSection(tokens[partitionSortingSection])
                                    .guidOfGroup(tokens[guidOfGroup])
                                    .briefDescriptionSection(tokens[briefDescriptionSection])
                                    .tags(tokens[tags])
                                    .elementActiviti(tokens[elementActiviti])
                                    .sortingOrderOfElement(tokens[sortingOrderOfElement])
                                    .elementTitle(tokens[elementTitle])
                                    .elementDescription(tokens[elementDescription])
                                    .elementKeywords(tokens[elementKeywords])
                                    .indexing(Boolean.parseBoolean(tokens[indexing]))
                                    .data(tokens[data])
                                    .dataOfPublication(tokens[dataOfPublication])
                                    .dataOfPublicationEnd(tokens[dataOfPublicationEnd])
                                    .pathImage(tokens[pathImage])
                                    .pathImageSmall(tokens[pathImageSmall])
                                    .shortcuts(tokens[shortcuts])
                                    .siteUserID(tokens[siteUserID])
                            .build()
                    );
                    System.out.println(resource.get(resource.size()-1).getNameOfElement() + ": ok");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        return resource;
    }

    private static ArrayList<OnceText> getOnceTexts(String[] tokens) {
        ArrayList<OnceText> onceTexts = new ArrayList<>();

        OnceText onceText1 = new OnceText(tokens[textBox1], Boolean.parseBoolean(tokens[textBox1_check]));
        OnceText onceText2 = new OnceText(tokens[textBox2], Boolean.parseBoolean(tokens[textBox2_check]));
        OnceText onceText3 = new OnceText(tokens[textBox3], Boolean.parseBoolean(tokens[textBox3_check]));
        onceTexts.add(onceText1);
        onceTexts.add(onceText2);
        onceTexts.add(onceText3);

        return onceTexts;
    }

    private static ArrayList<UrlInfo> getUrlInfos(String[] tokens) {
        ArrayList<UrlInfo> urlInfos = new ArrayList<>();

        UrlInfo urlInfo1 = null;
        UrlInfo urlInfo2 = null;
        UrlInfo urlInfo3 = null;
        UrlInfo urlInfo4 = null;
        UrlInfo urlInfo5 = null;
        try {
            urlInfo1 = new UrlInfo(SOURCE, new URL(tokens[urlInfo_link1]), tokens[urlInfo_title1], tokens[urlInfo_description1]);
            urlInfo2 = new UrlInfo(SOURCE, new URL(tokens[urlInfo_link2]), tokens[urlInfo_title2], tokens[urlInfo_description2]);
            urlInfo3 = new UrlInfo(SOURCE, new URL(tokens[urlInfo_link2]), tokens[urlInfo_title3], tokens[urlInfo_description3]);
            urlInfo4 = new UrlInfo(SOURCE, new URL(tokens[urlInfo_link4]), tokens[urlInfo_title4], tokens[urlInfo_description4]);
            urlInfo5 = new UrlInfo(SOURCE, new URL(tokens[urlInfo_link5]), tokens[urlInfo_title5], tokens[urlInfo_description5]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        urlInfos.add(urlInfo1);
        urlInfos.add(urlInfo2);
        urlInfos.add(urlInfo3);
        urlInfos.add(urlInfo4);
        urlInfos.add(urlInfo5);

        return urlInfos;
    }

}
