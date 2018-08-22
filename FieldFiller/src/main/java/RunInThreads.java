import com.ps.Page.Page;
import com.ps.Page.PageReader;
import com.ps.Threads.PageMaker;
import com.ps.Utils.Utilities;
import com.ps.searchEngines.Find;
import com.ps.searchEngines.Google;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RunInThreads {
    private static BlockingQueue<Page> rawPages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<Page> pages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<InetSocketAddress> proxies = new ArrayBlockingQueue<>(50);

    private static PageMaker[] pageMakers;

    private static final String tempFile = "FieldFiller/data/temp.csv";
    private static final String proxyFile = "Common/data/proxes-9.txt";
    private static final String tempGuidFile = "temp.guid.csv";
    private static final String inPutFileName = "test.csv";
    private static final String inPutPath = "FieldFiller/data/";
    private static final String outPutFileName = inPutFileName + ".out";
    private static final String outPutPath = "AddFields/result/";
    private static final int NUMBER_URL_IN_PAGE = 4;
    private static int numberOfPage;
    private static String header = "";

    private static long startTime;

    public static void main(String[] args) {
        Page page;

        int counterPage = 0;
        init();

        for (Page rawPage : rawPages) {
            System.out.println(String.format("%s, %s, %s",
                    rawPage.getSearchQuery(),
                    rawPage.getNameOfElement(),
                    rawPage.getElementTitle()
            ));
        }

        createProxyThreadPool(1);

        System.out.println("Number of page: " + numberOfPage);
    }

    private static void init() {
        startTime = System.currentTimeMillis();
        checkTempFile(tempFile);
        fillRawPage();
        fillProxies(proxyFile);

        numberOfPage = rawPages.size();
    }

    public static void checkTempFile(String tempFile) {

        if (!(new File(tempFile)).exists()) {
            removeWrapping(tempFile);
        }
    }

    private static void removeWrapping(String tempFile) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;

        StringBuilder cleanLine = new StringBuilder();
        String line;

        try {
            bufferedReader = new BufferedReader(new FileReader(inPutPath + inPutFileName));
            header = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty()) {
                    cleanLine.append(System.lineSeparator());
                    cleanLine.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert bufferedReader != null;
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        try {
            bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
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

    public static void fillRawPage() {
        ArrayList<Page> pages;
        ArrayList<Page> downPages;

        try {
            PageReader pageReader = new PageReader(tempFile, NUMBER_URL_IN_PAGE);
            pages = pageReader.read();
            rawPages.addAll(pages);
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
                            new ArrayList<>(),
                            3
                    ).build());
        }

        return pages;
    }

    public static void fillProxies(String fileName) {
        ArrayList<String> lines = Utilities.readResource(fileName);

        try {
            for (String line : lines) {
                String[] proxy = line.split(":");
                proxies.put(new InetSocketAddress(proxy[0], Integer.valueOf(proxy[1])));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void createProxyThreadPool(int numberCheckThreadPool) {
        pageMakers = new PageMaker[numberCheckThreadPool];
        ArrayList<Find> searchEngines = new ArrayList<>();
        searchEngines.add(new Google(30));

        for (int i = 0; i < numberCheckThreadPool; i++) {
            pageMakers[i] = new PageMaker(
                    searchEngines,
                    rawPages,
                    pages,
                    proxies,
                    true
            );
        }

        for (PageMaker pageMaker : pageMakers) {
            new Thread(pageMaker).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
