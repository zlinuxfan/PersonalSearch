import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Crater {

    private static Logger log = LoggerFactory.getLogger(Crater.class);

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;
    private static final int COUNTER_PAGES_IN_FILE = 15;

    private static final String filePrefix = "type_5_";

    private static final ArrayList<String> textsOfElements = readResource("data/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(CsvFileReader_Resource.readCsvFile("data/resource.csv"));

    public static void main(String[] args) {
        createPages();
    }


    private static void createPages() {
//        Page mainPage = CsvFileReader_Page.readCsvFile("data/mainResource.csv").get(0);
        Google google = new Google();
        ArrayList<Page> pages_1 = new ArrayList<>();
        ArrayList<Page> pages_2 = new ArrayList<>();
//        pages.add(mainPage);
        int counter = resources.size();
        int counterFiles = 1;

        for (Resource resource : resources) {

            System.out.println("(" + counter-- + ") Create page for request: " + resource.getNameRequest());

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

            ArrayList<OnceText> srcOnceTexts = elementOfPage
                    .getTextBoxes()
                    .stream()
                    .map(textBox -> new OnceText(textBox, false))
                    .collect(Collectors.toCollection(ArrayList::new));

            ArrayList[] onceTexts = createOnceTexts(srcOnceTexts);
            ArrayList[] urlInforms = createUrlInforms(elementOfPage.getUrlInfoList());

            Page page_1 = new Page.Builder(
                    "",
                    resource.getNameRequest(),
                    "",
                    resource.getTextOfElement(),
                    elementOfPage.getPath(),
                    onceTexts[0],
                    urlInforms[0]
            ).elementDescription(resource.getDescription())
                    .elementTitle(resource.getTitle())
                    .guidOfGroup("") //mainPage.getGuidOfGroup())
                    .build();

            Page page_2 = new Page.Builder(
                    "",
                    resource.getNameRequest(),
                    "",
                    resource.getTextOfElement(),
                    elementOfPage.getPath(),
                    onceTexts[1],
                    urlInforms[1]
            ).elementDescription(resource.getDescription())
                    .elementTitle(resource.getTitle())
                    .guidOfGroup("") //mainPage.getGuidOfGroup())
                    .build();

            pages_1.add(page_1);
            pages_2.add(page_2);

            if (pages_1.size() == COUNTER_PAGES_IN_FILE || pages_1.size() == resources.size()) {
                String fileName = ((counterFiles * COUNTER_PAGES_IN_FILE) - COUNTER_PAGES_IN_FILE + 1) +
                        "-" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE)) ;
                CsvFileWriter_Page.write("data/result/" + filePrefix +fileName+"_1.csv", pages_1);
                CsvFileWriter_Page.write("data/result/" + filePrefix +fileName+"_2.csv", pages_2);
                pages_1.clear();
                pages_2.clear();
                counterFiles++;
                System.out.println("File name: " + fileName);
            }
        }
    }

    private static ArrayList[] createUrlInforms(ArrayList<UrlInfo> src) {
        ArrayList[] dst = new ArrayList[2];
        ArrayList<UrlInfo> dst_1 = new ArrayList<>();
        ArrayList<UrlInfo> dst_2 = new ArrayList<>();

        if (src.size() >= NUMBER_ELEMENT) {
            dst_1.addAll(src.subList(0, NUMBER_ELEMENT));
            dst_2.addAll(src.subList(NUMBER_ELEMENT, src.size()));

            for (int size = dst_2.size(); size < NUMBER_ELEMENT; size++) {
                dst_2.add(new UrlInfo("", "", "", ""));
            }

            dst[0] = dst_1;
            dst[1] = dst_2;
        } else {
            for (int size = src.size(); size < NUMBER_ELEMENT; size++) {
                src.add(new UrlInfo("", "", "", ""));
            }
            for (int size = dst_2.size(); size < NUMBER_ELEMENT; size++) {
                dst_2.add(new UrlInfo("", "", "", ""));
            }

            dst[0] = dst_1;
            dst[1] = dst_2;
        }

        return dst;
    }
    private static ArrayList[] createOnceTexts(ArrayList<OnceText> src) {
        ArrayList[] dst = new ArrayList[2];
        ArrayList<OnceText> dst_1 = new ArrayList<>();
        ArrayList<OnceText> dst_2 = new ArrayList<>();


        if (src.size() >= NUMBER_TEXT_BOX) {
            dst_1.addAll(src.subList(0, NUMBER_TEXT_BOX));
            dst_2.addAll(src.subList(NUMBER_TEXT_BOX, src.size()));

            for (int size = dst_2.size(); size < NUMBER_TEXT_BOX; size++) {
                dst_2.add(new OnceText("", false));
            }

            dst[0] = dst_1;
            dst[1] = dst_2;
        } else {

            for (int size = src.size(); size < NUMBER_TEXT_BOX; size++) {
                src.add(new OnceText("", false));
            }

            for (int size = dst_2.size(); size < NUMBER_TEXT_BOX; size++) {
                dst_2.add(new OnceText("", false));
            }

            dst[0] = src;
            dst[1] = dst_2;
        }

        return dst;
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
