import Utils.GuidOfElement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Crater {

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;

    private static final ArrayList<String> textsOfElements = readResource("data/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(CsvFileReader_Resource.readCsvFile("data/resource.csv"));
    
    public static void main(String[] args) {

        int startGuidOfElement = 256;

        ArrayList<Page> pages = createPages(startGuidOfElement);

        CsvFileWriter_Page.write("data/result/" +
                startGuidOfElement +
                "-" +
                (startGuidOfElement + pages.size() - 2) +
                ".csv", pages);
    }


    private static ArrayList<Page> createPages(int startGuidOfElement) {
        GuidOfElement guidOfElement = new GuidOfElement(startGuidOfElement);
        Page mainPage = CsvFileReader_Page.readCsvFile("data/mainResource.csv").get(0);
        Google google = new Google();
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(mainPage);

        int counter = 0;

        for (Resource resource: resources) {

            if (counter >= 3) {
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

            for (int size = onceTexts.size(); size < NUMBER_TEXT_BOX; size++) {
                onceTexts.add(new OnceText("", false));
            }

            ArrayList<UrlInfo> urlInforms = elementOfPage.getUrlInfoList();

            for (int size = urlInforms.size(); size < NUMBER_ELEMENT; size++) {
                urlInforms.add(new UrlInfo("", "", "", ""));
            }

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
                    .guidOfGroup(mainPage.getGuidOfGroup())
                    .build();

            pages.add(page);

// If need one page in one file;
//            CsvFileWriter_Page.write(("data/result/" + String.valueOf(guidOfElement.getGuid()) + ".csv"), pages);
//            pages.remove(0);

            counter++;
        }

        return pages;
    }

    private static ArrayList<String> readResource(String fileName) {
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
