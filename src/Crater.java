import Utils.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;

public class Crater {

    private static Logger log = LoggerFactory.getLogger(Crater.class);

    private static final int NUMBER_TEXT_BOX = 3;
    private static final int NUMBER_ELEMENT = 5;
    private static final int COUNTER_PAGES_IN_FILE = 90;

    private static final boolean isTest = false;

    private static final String filePrefix = "garden";
    private static final String resourceManagement = "cycle";

    private static final ArrayList<String> textsOfElements = Utilities.readResource("data/" +filePrefix+ "/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(
            CsvFileReader_Resource.readCsvFile("data/"+filePrefix+"/resource.csv"),
            resourceManagement
    );

    public static void main(String[] args) {
        createPages();
    }

    private static void createPages() {

        Google google = new Google();
        ArrayList<Page> pages_1 = new ArrayList<>();
        ArrayList<Page> pages_2 = new ArrayList<>();
        Random random = new Random();

        int counter = resources.size();
        int counterFiles = 1;
        int counterTest = 0;
        int timeOut;
        int remainderPages = resources.size();

        for (Resource resource : resources) {
            remainderPages--;

            System.out.println("(" + counter-- + ") Create page for request: " + resource.getNameRequest());

            ArrayList<UrlInfo> urlInfos;
            try {
                timeOut = random.nextInt(120);
                System.out.println("Timeout: " + timeOut + "sec ...");
                Thread.sleep(timeOut * 1000);
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

            if (pages_1.size() == COUNTER_PAGES_IN_FILE || remainderPages == 0) {
                String fileName = filePrefix + "_" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE) - COUNTER_PAGES_IN_FILE + 1) +
                        "-" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE));
                CsvFileWriter_Page.write("data/" +filePrefix + "/result/" + fileName + "_1.csv", pages_1);
                CsvFileWriter_Page.write("data/" +filePrefix + "/result/"  + fileName + "_2.csv", pages_2);
                pages_1.clear();
                pages_2.clear();
                counterFiles++;
                System.out.println("Files name: " + fileName + "_1.csv" + ", " + fileName + "_2.csv");
            }
            if (isTest  && counterTest > COUNTER_PAGES_IN_FILE) {
                break;
            }
            counterTest++;
        }
        if (ElementOfPage.getBedUrls().size() > 0) {
            Utilities.writeResource(ElementOfPage.getBedUrls(), "/bedUrl.txt");
        }
    }

//    private static ArrayList[] dividedIntoTwo(ArrayList<?> src, int numberOfElements) {
//        ArrayList[] dst = new ArrayList[2];
//        ArrayList<?> dst_1 = new ArrayList<>();
//        ArrayList<?> dst_2 = new ArrayList<>();
//
//        return dst;    }

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

    private static ArrayList<Resource> modifyResource(ArrayList<Resource> rawResources, String type) {
        ArrayList<Resource>resources;
        switch (type) {
            case "cycle":
                resources = cycleModifyResource(rawResources);
                break;
            case "select":
                // In a row, removing the used ones
                resources = selectModifyResource(rawResources);
                break;
            case "random":
                resources = randomModifyResource(rawResources);
                break;
            default:
                throw new IllegalArgumentException("This type " + type + " is not supported");
        }
        return resources;
    }

    private static ArrayList<Resource> randomModifyResource(ArrayList<Resource> rawResources) {
        throw new UnsupportedOperationException("Type \"random\" is not support.");
    }

    private static ArrayList<Resource> cycleModifyResource(ArrayList<Resource> rawResources) {
        int counter = 0;
        for (Resource resource: rawResources) {
            if (counter >= textsOfElements.size()) {
                counter = 0;
            }
            resource.setTextOfElement(textsOfElements.get(counter++).replace("хх1хх", resource.getPhraseOfElement()));
        }

        return rawResources;
    }

    private static ArrayList<Resource> selectModifyResource(ArrayList<Resource> rawResources) {
        if (rawResources.size() > textsOfElements.size()) {
            throw new IllegalArgumentException("Lacks data in file textOfElement.txt");
        }

        Iterator<String> iterator = textsOfElements.iterator();

        for (Resource resource : rawResources) {
            String text = iterator.next();
            resource.setTextOfElement(text.replace("хх1хх", resource.getPhraseOfElement()));
            iterator.remove();
        }

        Utilities.writeResource(textsOfElements, filePrefix + "/textsOfElements.txt");
        return rawResources;
    }
}
