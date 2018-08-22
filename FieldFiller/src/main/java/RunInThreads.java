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
import java.util.concurrent.TimeUnit;

public class RunInThreads {
    private static BlockingQueue<Page> rawPages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<Page> pages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<InetSocketAddress> proxies = new ArrayBlockingQueue<>(50);

    private static PageMaker[] pageMakers;

    private static final String tempFile = "FieldFiller/data/temp.csv";
    private static final String proxyFile = "Common/data/proxes-9.txt";
    private static final String tempSearchQueryFile = "temp.SearchQuery.csv";
    private static final String inPutFileName = "test.csv";
    private static final String inPutPath = "FieldFiller/data/";
    private static final String outPutFileName = inPutFileName + ".out";
    private static final String outPutPath = "FieldFiller/result/";
    private static final int NUMBER_URL_IN_PAGE = 4;
    private static int numberOfPage;
    private static String header = "";
    private static String headerOutput = "Название элемента;Заголовок (title);Файл изображения для элемента;Адрес (youtube);Заголовок1-1;Ссылка1-1;Описание1-1;Заголовок2-1;Ссылка2-1;Описание2-1;Заголовок3-1;Ссылка3-1;Описание3-1;Заголовок4-1;Ссылка4-1;Описание4-1";
    private static String headerTemp = "Поисковый запрос";

    private static long startTime;

    public static void main(String[] args) {
        Page page;

        int counterPage = 0;
        init();
        createProxyThreadPool(1);

        if (numberOfPage > 0) {
            System.out.println("Left pages: " + numberOfPage + ".");
        } else {
            System.out.println("All pages are collected for " + inPutFileName + ". See " + outPutPath + outPutFileName);
            System.exit(0);
        }

        do {
            try {
                page = pages.poll(1000, TimeUnit.MILLISECONDS);
                if (page != null) {
                    if (page.getUrlInfoList().size() > 0) {
                        Utilities.writeShortPageInFile(outPutPath + outPutFileName, page, true);
                        Utilities.writeStringInFile(outPutPath + tempSearchQueryFile, page.getSearchQuery(), true);
                        counterPage++;
                        System.out.println(String.format("%s pcs remaining", numberOfPage - counterPage));
                    } else {
                        System.out.println(String.format("Page id: %s. UrlInfoList size: %d",
                                page.getGuidOfElement(),
                                page.getUrlInfoList().size()));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (isRunThreads() && counterPage < numberOfPage);

        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static void printPage(Page page) {
            System.out.println(String.format("%s, %s, %s",
                    page.getSearchQuery(),
                    page.getNameOfElement(),
                    page.getElementTitle()
            ));
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

        if (new File(outPutPath + outPutFileName).exists() &&
                new File(outPutPath + tempSearchQueryFile).exists()) {
            downPages = readPagesInF(outPutPath + tempSearchQueryFile);
            rawPages.removeAll(downPages);
        } else {
            Utilities.writeStringInFile(outPutPath + outPutFileName, headerOutput, true);
            Utilities.writeStringInFile(outPutPath + tempSearchQueryFile, headerTemp, true);
        }
    }

    private static ArrayList<Page> readPagesInF(String fileName) {
        ArrayList<String> searchQueryList = Utilities.readResource(fileName);
        ArrayList<Page> pages = new ArrayList<>();

        for (String searchQuery : searchQueryList) {

            pages.add(
                    new Page.Builder(
                            null,
                            "", //("Название элемента"),
                            "", //("Описание элемента"),
                            "", //("Текст для элемента"),
                            "", //("Путь к элементу"),
                            null,
                            new ArrayList<>(),
                            NUMBER_URL_IN_PAGE
                    )
                            .searchQuery(searchQuery)
                            .build());
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

    private static boolean isRunThreads() {
        boolean isRun = false;

        for (PageMaker googleThread : pageMakers) {
            isRun = isRun || googleThread.isRunning();
        }

        return isRun;
    }
}
