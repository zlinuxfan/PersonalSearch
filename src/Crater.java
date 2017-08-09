import Utils.GuidOfElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Crater {

    private static Logger log = LoggerFactory.getLogger(Crater.class);

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;
    private static final int COUNTER_PAGES_IN_FILE = 15;

    private static final ArrayList<String> textsOfElements = readResource("data/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(CsvFileReader_Resource.readCsvFile("data/resource.csv"));

    public static void main(String[] args) {

        int startGuidOfElement = 61;

        createPages(startGuidOfElement);

    }


    private static void createPages(int startGuidOfElement) {
        GuidOfElement guidOfElement = new GuidOfElement(startGuidOfElement);
//        Page mainPage = CsvFileReader_Page.readCsvFile("data/mainResource.csv").get(0);
        Google google = new Google();
        ArrayList<Page> pages = new ArrayList<>();
//        pages.add(mainPage);
        int counter = resources.size();
        int counterFiles = 1;

        for (Resource resource : resources) {

            System.out.println("(" + counter-- + ") Create page for request: " + resource.getNameRequest());
            resource.setProcessedNameRequest(
                    "#" +
                            guidOfElement.getGuid() +
                            resource.getNameRequest() + ";" +
                            resource.getTitle() + ";" +
                            resource.getDescription() + ";" +
                            resource.getTextOfElement()
            );

            ArrayList<UrlInfo> urlInfos;
            try {
                urlInfos = google.find(resource.getNameRequest());
            } catch (Exception e) {
                log.error("    error: \"" + resource.getNameRequest() + "\" is not processed. Check internet or capcha.");
                continue;
            }

            ElementOfPage elementOfPage = new ElementOfPage(
                    resource.getNameRequest(),
                    urlInfos
            );

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
                    "",
                    resource.getNameRequest(),
                    "",
                    resource.getTextOfElement(),
                    elementOfPage.getPath(),
                    onceTexts,
                    urlInforms
            ).elementDescription(resource.getDescription())
                    .elementTitle(resource.getTitle())
                    .guidOfGroup("") //mainPage.getGuidOfGroup())
                    .build();

            pages.add(page);

            if (pages.size() == COUNTER_PAGES_IN_FILE || pages.size() == resources.size()) {
                String fileName = ((counterFiles * COUNTER_PAGES_IN_FILE) - COUNTER_PAGES_IN_FILE + 1) +
                        "-" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE)) +
                        ".csv";
                CsvFileWriter_Page.write("data/result/" + fileName, pages);
                pages.clear();
                counterFiles++;
                System.out.println("File name: " + fileName);
            }

// If need one page in one file;
//            CsvFileWriter_Page.write(("data/result/" + String.valueOf(guidOfElement.getGuid()) + ".csv"), pages);
//            pages.remove(0);

        }
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
        } catch (Exception e) {
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

    private static void writeResource(ArrayList<String> textsOfElements) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("data/textsOfElements.txt");
            for (String str : textsOfElements) {
                fileWriter.append(str);
                fileWriter.append("\n");
            }
            System.out.println("File textOfElement.txt rewrite successfully !!!");
        } catch (IOException e) {
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

    private static ArrayList<Resource> modifyResource(ArrayList<Resource> rawResources) {

        if (rawResources.size() > textsOfElements.size()) {
            throw new IllegalArgumentException("Lacks data in file textOfElement.txt");
        }

        Iterator<String> iterator = textsOfElements.iterator();

        for (Resource resource : rawResources) {
            String text = iterator.next();
            resource.setTextOfElement(text.replace("хх1хх", resource.getPhraseOfElement()));
            iterator.remove();
        }

        writeResource(textsOfElements);

        return rawResources;
    }
}
