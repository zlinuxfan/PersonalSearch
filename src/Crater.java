import java.util.ArrayList;
import java.util.List;

public class Crater {

    public static void main(String[] args) {
        String requestName = "Как сделать бумажный меч";

        Google google = new Google();
        ElementOfPage elementOfPage = new ElementOfPage(requestName, google.find(requestName));

        ArrayList<OnceText> onceTexts = new ArrayList<>();
        onceTexts.add(new OnceText(elementOfPage.getTextBoxs().get(0), false));

        ArrayList<UrlInfo> urlInfos = elementOfPage.getUrlInfoList();

        Page page = new Page.Builder("0", requestName, "", "", "", onceTexts, "", urlInfos).build();
        List<Page> pages = new ArrayList<>();
        pages.add(page);

        CsvFileWriter.write("test.csv", pages);
        printElementOfPages(elementOfPage);

    }

    private static void printElementOfPages(ElementOfPage elementOfPage) {
        System.out.println("name: " + elementOfPage.getName());
        System.out.println("path: " + elementOfPage.getPath());

        elementOfPage.getTextBoxs().forEach(System.out::println);

        for (UrlInfo urlInfo: elementOfPage.getUrlInfoList()) {
            System.out.print(urlInfo.getLink());
            System.out.print(" Is youtube: " + urlInfo.isYoutube());
            System.out.println(" Source: " + urlInfo.getSource());
            System.out.println(urlInfo.getHeading());
            System.out.println(urlInfo.getDescription());
            System.out.println();
        }
    }
}
