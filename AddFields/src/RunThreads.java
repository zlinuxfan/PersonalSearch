
import Utils.Utilities;
import com.Page;
import com.ps.Page.PageReader;
import com.ps.Threads.GoogleThread;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RunThreads {
    private static BlockingQueue<Page> rawPages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<Page> pages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<InetSocketAddress> proxies = new ArrayBlockingQueue<>(30);

    private static GoogleThread[] googleThreads;

    private static final String tempFile = "AddFields/data/result/temp.csv";
    private static final String tempGuidFile = "temp.guid.csv";
    private static final String inPutFileName = "kak-lechit-utf-work.csv";
    private static final String inPutPath = "AddFields/data/";
    private static final String outPutFileName = inPutFileName + ".out";
    private static final String outPutPath = "AddFields/data/result/";
    private static int numberOfPage;
    private static String header = "";

    private static long startTime;

    private static void init() {
        startTime = System.currentTimeMillis();
        checkTempFile(tempFile);
        fillRawPage();
        fillProxies();

        numberOfPage = rawPages.size();
    }

    public static void main(String[] args) {
        Page page;
        int counterPage = 0;
        init();
        createCheckThreadPool(5);

        do {
            try {
                page = pages.poll(3000, TimeUnit.MILLISECONDS);
                if (page != null) {
                    Utilities.writeShortPageInFile(outPutPath + outPutFileName, page, true);
                    Utilities.writeStringInFile(outPutPath + tempGuidFile, page.getGuidOfElement(), true);
                    counterPage++;
                    System.out.println(String.format("%s pcs remaining", numberOfPage - counterPage));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (isRunThreads() && counterPage < numberOfPage);

        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static boolean isRunThreads() {
        boolean isRun = false;

        for (GoogleThread googleThread : googleThreads) {
            isRun = googleThread.isRunning();
        }

        return isRun;
    }

    private static void createCheckThreadPool(int numberCheckThreadPool) {
        googleThreads = new GoogleThread[numberCheckThreadPool];

        for (int i = 0; i < numberCheckThreadPool; i++) {
            googleThreads[i] = new GoogleThread(
                    20,
                    rawPages,
                    pages,
                    proxies,
                    true
            );
        }

        for (GoogleThread googleThread : googleThreads) {
            new Thread(googleThread).start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void fillProxies() {
        try {
            proxies.put(new InetSocketAddress("62.109.8.114", 443));
            proxies.put(new InetSocketAddress("94.250.255.31", 443));
//            proxies.put(new InetSocketAddress("149.154.71.37", 443));
            proxies.put(new InetSocketAddress("82.146.40.45", 443));
            proxies.put(new InetSocketAddress("188.120.246.178", 443));
            proxies.put(new InetSocketAddress("78.24.223.92", 443));
            proxies.put(new InetSocketAddress("185.127.166.200", 443));
            proxies.put(new InetSocketAddress("37.230.138.173", 443));
            proxies.put(new InetSocketAddress("185.127.165.122", 443));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void fillRawPage() {
        ArrayList<Page> pages;
        ArrayList<Page> downPages;

        try {
            PageReader pageReader = new PageReader(tempFile);
            pages = pageReader.read();
            rawPages.addAll(pages);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (new File(outPutPath + outPutFileName).exists() &&
                new File(outPutPath + tempGuidFile).exists()) {
            downPages = readPagesInF(outPutPath + tempGuidFile);
            rawPages.removeAll(downPages);
        } else {
            Utilities.writeStringInFile(outPutPath + outPutFileName, header, true);
            Utilities.writeStringInFile(outPutPath + tempGuidFile, header, true);
        }
    }

    private static void checkTempFile(String tempFile) {

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
}
