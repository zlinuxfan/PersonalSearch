import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Crater {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");

    public static void main(String[] args) {
        String requestName = "Как сделать бумажный меч";

        Google google = new Google();
        ElementOfPage elementOfPage = new ElementOfPage(requestName, google.find(requestName));

        ArrayList<OnceText> onceTexts = elementOfPage.getTextBoxes().stream().map(textBox -> new OnceText(textBox, false)).collect(Collectors.toCollection(ArrayList::new));


        ArrayList<UrlInfo> urlInfos = elementOfPage.getUrlInfoList();

        Page page = new Page.Builder("0", requestName, "", "", elementOfPage.getPath(), onceTexts, urlInfos).build();
        List<Page> pages = new ArrayList<>();
        pages.add(page);

        CsvFileWriter.write(sdf.format(System.currentTimeMillis())+".csv", pages);
//        printElementOfPages(elementOfPage);

    }

    private static void printElementOfPages(ElementOfPage elementOfPage) {
        System.out.println("name: " + elementOfPage.getName());
        System.out.println("path: " + elementOfPage.getPath());

        elementOfPage.getTextBoxes().forEach(System.out::println);

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
