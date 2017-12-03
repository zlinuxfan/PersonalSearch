import Utils.Utilities;
import com.Page;
import com.UrlInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Thread.sleep;

public class Run {

    private static Logger log = LoggerFactory.getLogger(Run.class);
    private static Random random = new Random();
    //    private static final int COUNTER_PAGES_IN_FILE = 97;
    //    private static final String outPutFileName = "test.csv";
    private static final String tempFileName = "temp.csv";
    private static final String inPutFileName = "preparation-utf.csv";
    private static final String outPutFileName = inPutFileName + ".out";
    private static final String outPutPath = "AddFields/data/result/";

    private static ArrayList<String> header = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Star work: " + Utilities.convertToTime(startTime));

        if (! new File(outPutPath + tempFileName).exists()) {
            removeWrapping("AddFields/data/" + inPutFileName);
        }

        header = readHeader(outPutPath + tempFileName);

        ArrayList<Page> rawPages = readPagesInFile(outPutPath + tempFileName);
        ArrayList<Page> downPages = new ArrayList<>();

        if (new File(outPutPath + outPutFileName).exists()) {
            downPages = readPagesInF(outPutPath + "temp.guid.txt");
            rawPages.removeAll(downPages);
        }

        System.out.println("rawPages: " + rawPages.size() + ". DownPages: " + downPages.size());

        File file = new File(outPutPath + outPutFileName);
        if (!file.exists()) {
            Utilities.writeShortHeaderInFile(outPutPath + outPutFileName);
        }

        cratePagesAndWrite(rawPages);

        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static ArrayList<String> readHeader(String fileName) {
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

        assert header != null;
        for (String fieldName : header.iterator().next()) {
            headers.add(fieldName);
        }
        return headers;
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
                    if (line.startsWith(";") || line.startsWith("\"")) {
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

        writeToFile(header, cleanLine.toString());
    }

    private static void cratePagesAndWrite(ArrayList<Page> pages) {
        int NumberPages = pages.size();
        Google google = new Google();
        Iterator<Page> pageIterator = pages.listIterator();

        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            System.out.println("(" + NumberPages-- + " ) Make: " + page.getNameOfElement() + " ...");

            try {
                fillPage(page, google);
            } catch (Exception e) {
//                log.error("    error: \"" + page.getNameOfElement() + "\" is not processed. Check internet or captcha.");
                System.out.println("    error: \"" + page.getNameOfElement() + "\" is not processed. Check internet or captcha.");
                e.printStackTrace();
                // not work
//                CsvFileWriter_Page.write(outPutPath + tempFileName + "_", pages);
                break;
            }

            checkYouTube(page, google);

            Utilities.writeShortPageInFile(outPutPath + outPutFileName, page, true);
            Utilities.writeStringInFile(outPutPath + "temp.guid.txt", page, true);
            pageIterator.remove();
        }
    }

    private static void fillPage(Page page, Google google) throws Exception {

        ArrayList<UrlInfo> urlInfos = google.find(page.getNameOfElement());
        makeDelay();


        int index = 0;
        while (urlInfos.size() > index) {
            if (page.getUrlInfoList().size() < 5 && !urlInfos.get(index).isYoutube()) {
                page.addUrlList(urlInfos.get(index));
            } else {
                if (page.getIdYouTube().isEmpty() && urlInfos.get(index).isYoutube()) {
                    page.setPathYouTube(urlInfos.get(index).getLink().toString());
                }
            }
            index++;
        }
    }

    private static void checkYouTube(Page page, Google google) {
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

    private static void writePagesIntoFile(String fileName, ArrayList<Page> pages) {

            try (
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));

                    CSVPrinter csvPrinter = new CSVPrinter(
                            writer,
                            CSVFormat.DEFAULT.withHeader("ID", "Name", "Designation", "Company"))
            ) {
                for (Page page : pages) {
                    csvPrinter.printRecord(page.toString());
                }
                csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
                csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
                csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

                csvPrinter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static ArrayList<Page> readPagesInF(String fileName) {
        ArrayList<String> guids = Utilities.readResource(fileName);
        ArrayList<Page> pages = new ArrayList<>();

        for (String guid : guids) {

            pages.add(
                    new Page.Builder(
                            guid,
                            "", //("Название элемента"),
                            "", //("Описание элемента"),
                            "", //("Текст для элемента"),
                            "", //("Путь к элементу"),
                            null,
                            new ArrayList<>()
                    ).build());
        }

        return pages;
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

            if (record.isMapped("Ссылка1-1")) {
                urlInfos.add(
                        new UrlInfo(
                                "file.csv",
                                record.get("Ссылка1-1"),
                                record.get("Заголовок1-1"),
                                record.get("Описание1-1")
                        )
                );
            }

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

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pages;
    }

    private static void writeToFile(String header, String dataToFile) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outPutPath + tempFileName);
            fileWriter.append(header);
            fileWriter.append(dataToFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
