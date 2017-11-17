import Utils.*;
import com.Page;
import com.UrlInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Run {

    private static Logger log = LoggerFactory.getLogger(Run.class);
    private static Random random = new Random();
    //    private static final int COUNTER_PAGES_IN_FILE = 97;
    private static final String outPutFileName = "unloadingOfGarden11-15_utf.csv";
    //    private static final String outPutFileName = "test.csv";
    private static final String tempFileName = "temp.csv";
    private static final String inPutFileName = outPutFileName;
    private static final String outPutPath = "AddFields/data/result/";

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Star work: " + Utilities.convertToTime(startTime));

        File dataFile = new File(outPutPath + tempFileName);
        if (!dataFile.exists()) {
            removeWrapping("AddFields/data/" + inPutFileName);
        }

        ArrayList<Page> pages = readPagesInFile(outPutPath + tempFileName);

        File file = new File(outPutPath + outPutFileName);
        if (!file.exists()) {
            Utilities.writeShortHeaderInFile(outPutPath + outPutFileName);
        }

        cratePagesAndWrite(pages);

        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static void removeWrapping(String inPutFileName) {
        BufferedReader reader = null;
        BufferedWriter bufferedWriter = null;
        StringBuilder cleanLine = new StringBuilder();
        String header = "";

        try {
            reader = new BufferedReader(new FileReader(inPutFileName));
            header = reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.isEmpty()) {
                    if (line.startsWith(";ID")) {
                        cleanLine.append(System.lineSeparator());
                        cleanLine.append(line);
                    } else {
                        cleanLine.append(line);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(outPutPath + tempFileName));
            bufferedWriter.write(header);
            bufferedWriter.write(cleanLine.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void cratePagesAndWrite(ArrayList<Page> pages) {
        int NumberPages = pages.size();
        Google google = new Google();
        ArrayList<UrlInfo> urlInfos;

        for (Page page : pages) {
            System.out.println("(" + NumberPages-- + " ) Make: " + page.getNameOfElement() + " ...");

            try {
                if (page.getIdYouTube().isEmpty()) {
                    System.out.println("youtube is empty ...");
                    ArrayList<String> youTubes = google.findYouTube(page.getNameOfElement(), 1, 10);

                    if (!youTubes.isEmpty()) {
                        page.setPathYouTube(youTubes.get(0));
                        System.out.println("make youtube link ...");
                    } else {
                        System.out.println("This is absent youtube link.");
                    }
                    makeDelay();
                }
            } catch (Exception e) {
//                log.error("For \"" + page.getNameOfElement() + "\" do not create youTube Id.");
                e.printStackTrace();
                page.setIndexing(false);
            }

            try {
                urlInfos = google.find(page.getNameOfElement());
                makeDelay();
            } catch (Exception e) {
//                log.error("    error: \"" + page.getNameOfElement() + "\" is not processed. Check internet or captcha.");
                System.out.println("    error: \"" + page.getNameOfElement() + "\" is not processed. Check internet or captcha.");
                e.printStackTrace();
                break;
            }


            int index = 0;
            while (urlInfos.size() < index || page.getUrlInfoList().size() < 5) {
                if (!urlInfos.get(index).isYoutube()) {
                    page.addUrlList(urlInfos.get(index));
                }
                index++;
            }

            Utilities.writePageInFile(outPutPath + outPutFileName, page, true);

        }
    }

    private static void makeDelay() {
        long timeOut = random.nextInt(57);
        System.out.println("Timeout: " + timeOut + "sec ...");
        try {
            sleep(timeOut * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Page> readPagesInFile(String fileName) {

        Reader in = null;
        try {
            in = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterable<CSVRecord> header = null;
        try {
            assert in != null;
            header = CSVFormat.DEFAULT.withDelimiter(';').parse(in);
        } catch (IOException e) {
            System.out.println("Can not parse header in csv file." + Arrays.toString(e.getStackTrace()));
        }

        ArrayList<String> headers = new ArrayList<>();
        ArrayList<Page> pages = new ArrayList<>();
        int counterLine = 1;

        assert header != null;
        for (String fieldName : header.iterator().next()) {
            headers.add(fieldName);
        }

        String[] headerStrings = new String[headers.size()];
        headerStrings = headers.toArray(headerStrings);
        CSVParser records = null;
        try {
            records = CSVFormat.DEFAULT.withDelimiter(';').withHeader(headerStrings).parse(in);
        } catch (IOException e) {
            System.out.println("Can not parse line csv file." + Arrays.toString(e.getStackTrace()));
        }

        assert records != null;

        for (CSVRecord record : records) {

            if (headers.size() != record.size()) {
                System.out.println("Error in " + counterLine + " line." + "Number row in header: " + headers.size() + " > number row in line: " + record.size() + ".");
                continue;
            }
            ArrayList<UrlInfo> urlInfos = new ArrayList<>();
            urlInfos.add(
                    new UrlInfo(
                            "file.csv",
                            record.get("Ссылка1-1"),
                            record.get("Заголовок1-1"),
                            record.get("Описание1-1")
                    )
            );

            pages.add(
                    new Page.Builder(
                            record.get("GUID идентификатор элемента"),
                            record.get("Название элемента"),
                            record.get("Описание элемента"),
                            record.get("Текст для элемента"),
                            record.get("Путь к элементу"),
                            null,
                            urlInfos
                    ).idYouTube(
                            record.get("Адрес (youtube)")
                    ).build());

            counterLine++;
        }

        return pages;
    }
}
