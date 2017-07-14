import Utils.GuidOfElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Crater {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
    private static final ArrayList<String> textsOfElements = readFile("data/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(CsvFileReader_Resource.readCsvFile("data/resource.csv"));
    
    public static void main(String[] args) {

        Google google = new Google();
        List<Page> pages = new ArrayList<>();

        int counter = 0;

        for (Resource resource: resources) {

            if (counter >= 1) {
                break;
            }

            System.out.println("Create page for request: " + resource.getNameRequest());

            ElementOfPage elementOfPage = new ElementOfPage(
                    resource.getNameRequest(),
                    google.find(resource.getNameRequest()
                    ));

            ArrayList<OnceText> onceTexts = elementOfPage
                    .getTextBoxes()
                    .stream()
                    .map(textBox -> new OnceText(textBox, false))
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList<UrlInfo> urlInforms = elementOfPage.getUrlInfoList();

            GuidOfElement guidOfElement = new GuidOfElement(0);

            Page page = new Page.Builder(
                    String.valueOf(guidOfElement.next()),
                    resource.getNameRequest(),
                    "",
                    resource.getTextOfElement(),
                    elementOfPage.getPath(),
                    onceTexts,
                    urlInforms
                ).elementDescription(resource.getDescription())
                    .elementTitle(resource.getTitle())
                    .build();

            pages.add(page);

            counter++;
        }
        
        CsvFileWriter_Page.write(sdf.format(System.currentTimeMillis())+".csv", pages);

    }

    private static ArrayList<String> readFile(String fileName) {
        BufferedReader fileReader = null;
        ArrayList<String> textsOfElements = new ArrayList<>();

        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {
                textsOfElements.add(line);
            }
        }
        catch (Exception e) {
            System.out.println("Error in FileReader !!!");
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

        return textsOfElements;
    }

    private static ArrayList<Resource> modifyResource(ArrayList<Resource> rawResources) {
        int counter = 0;
        for (Resource resource: rawResources) {
            if (counter >= textsOfElements.size()) {
                counter = 0;
            }
            resource.setTextOfElement(textsOfElements.get(counter++).replace("хх1хх", resource.getPhraseOfElement()));
        }
        return rawResources;
    }
}
