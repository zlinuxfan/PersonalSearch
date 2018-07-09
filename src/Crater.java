import Utils.Utilities;
import com.OnceText;
import com.Page;
import com.UrlInfo;
import com.ps.searchEngines.Google;
import com.ps.searchEngines.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;

public class Crater {

    private static Logger log = LoggerFactory.getLogger(Crater.class);

    private static final int COUNTER_PAGES_IN_FILE = 97;
    private static final int NUMBER_URL_PAGE = 5;

    private static final boolean isTest = false;

    private static final String filePath = "cook/";
    private static final String filePrefix = "salads_3";
    private static final String resourceManagement = "random";

    private static final ArrayList<String> textsOfElements = Utilities.readResource(
            "data/" + filePath + filePrefix + "/textsOfElements.txt");
    private static final ArrayList<Resource> resources = modifyResource(
            CsvFileReader_Resource.readCsvFile(
                    "data/" + filePath + filePrefix + "/resource.csv"),
            resourceManagement
    );

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Star work: " + Utilities.convertToTime(startTime));
        createPages();
        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static void createPages() {

        Google google = new Google(10);
        ArrayList<Page> pages = new ArrayList<>();
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
            ArrayList<Picture> pictures;

            try {
                pictures = google.findPicture(resource.getNameRequest(), 10, new InetSocketAddress("localhost", 1111));

                timeOut = random.nextInt(70);
                System.out.println("Timeout: " + timeOut + "sec ...");
                Thread.sleep(timeOut * 1000);

                urlInfos = google.find(resource.getNameRequest(), new InetSocketAddress("localhost", 1111));
            } catch (Exception e) {
                log.error("    error: \"" + resource.getNameRequest() + "\" is not processed. Check internet or captcha.");
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

            srcOnceTexts.get(0).setCheckBox(false);
            String pathImage = "";
            for (Picture picture : pictures) {
                if (picture.getProtocol().equals("http")) {
                    pathImage = picture.getUrl();
                    break;
                }
            }

            Page page = new Page.Builder(
                    "",
                    resource.getNameRequest(),
                    "",
                    resource.getTextOfElement(),
                    elementOfPage.getPath(),
                    srcOnceTexts,
                    elementOfPage.getUrlInfoList(),
                    NUMBER_URL_PAGE
            ).elementDescription(resource.getDescription())
                    .elementTitle(resource.getTitle())
                    .guidOfGroup("") //mainPage.getGuidOfGroup())
                    .pathImage(pathImage)
                    .pathImageSmall(pathImage)
                    .build();

            if (page.getIdYouTube().isEmpty()) {
                try {
                    System.out.println("Timeout YouTube: " + timeOut + "sec ...");
                    try {
                        Thread.sleep(timeOut * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    page.setPathYouTube(google.findYouTube(resource.getNameRequest(), 1, new InetSocketAddress("localhost", 1111)).get(0));
                } catch (Exception e) {
                    log.error("For \"" + resource.getNameRequest() + "\" do not create youTube Id.");
                    page.setIndexing(false);
                }
            }

            if (page.getPathImage().isEmpty() || page.getTextBoxes().size() == 0) {
                page.setIndexing(false);
            }

            pages.add(page);

            if (pages.size() == COUNTER_PAGES_IN_FILE || remainderPages == 0) {
                String fileName = filePrefix + "_" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE) - COUNTER_PAGES_IN_FILE + 1) +
                        "-" +
                        ((counterFiles * COUNTER_PAGES_IN_FILE) - COUNTER_PAGES_IN_FILE + pages.size());
                CsvFileWriter_Page.write("data/" + filePath + filePrefix + "/result/" + fileName + ".csv", pages);
                pages.clear();
                counterFiles++;
                System.out.println("Files name: " + fileName + ".csv");
            }
            if (isTest && counterTest > COUNTER_PAGES_IN_FILE) {
                break;
            }
            counterTest++;
        }

        if (ElementOfPage.getBedUrls().size() > 0) {
            Utilities.writeResource(ElementOfPage.getBedUrls(), "/bedUrl.txt", true);
        }

        HashSet<String> blackBedUrl = new HashSet<>(Utilities.readResource("data/bedUrl.txt"));
        ArrayList<String> clearBedUrlList = new ArrayList<>();
        clearBedUrlList.addAll(blackBedUrl);
        Utilities.writeResource(clearBedUrlList, "/bedUrl.txt", false);
    }

    private static ArrayList<Resource> modifyResource(ArrayList<Resource> rawResources, String type) {
        ArrayList<Resource> resources;
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
        Random random = new Random();

        for (Resource resource : rawResources) {

            resource.setTextOfElement(textsOfElements.get(random.nextInt(textsOfElements.size())).replace("хх1хх", resource.getCommonQuestion()));
        }

        return rawResources;
    }

    private static ArrayList<Resource> cycleModifyResource(ArrayList<Resource> rawResources) {
        int counter = 0;
        for (Resource resource : rawResources) {
            if (counter >= textsOfElements.size()) {
                counter = 0;
            }
            resource.setTextOfElement(textsOfElements.get(counter++).replace("хх1хх", resource.getCommonQuestion()));
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
            resource.setTextOfElement(text.replace("хх1хх", resource.getCommonQuestion()));
            iterator.remove();
        }

        Utilities.writeResource(textsOfElements, filePath + filePrefix + "/textsOfElements.txt", false);
        return rawResources;
    }
}
