import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Crater {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");

    public static void main(String[] args) {
        String requestName = "Как сделать бумажный меч";

        Google google = new Google();

        ArrayList<Resource> resources = CsvFileReader_Resource.readCsvFile("resource.csv");

//        ElementOfPage elementOfPage = new ElementOfPage(requestName, google.find(requestName));
//
//        ArrayList<OnceText> onceTexts = elementOfPage.getTextBoxes().stream().map(textBox -> new OnceText(textBox, false)).collect(Collectors.toCollection(ArrayList::new));
//
//
//        ArrayList<UrlInfo> urlInfos = elementOfPage.getUrlInfoList();
//
//        Page page = new Page.Builder("0", requestName, "", "", elementOfPage.getPath(), onceTexts, urlInfos).build();
//        List<Page> pages = new ArrayList<>();
//        pages.add(page);
//
//        CsvFileWriter_Page.write(sdf.format(System.currentTimeMillis())+".csv", pages);

    }
}
