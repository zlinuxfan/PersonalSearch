import Utils.Utilities;
import com.Page;
import com.ps.Page.PageReader;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class testForThread {
    private static BlockingQueue<Page> rawPages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<Page> pages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<InetSocketAddress> proxies = new ArrayBlockingQueue<>(30);

    private static final String tempFile = "testForThread/data/result/temp.csv";
    private static final String inPutFileName = "unloadingOfGarden11-15_utf.csv";
    private static final String inPutPath = "testForThread/data/";
    private static final String outPutFileName = inPutFileName + ".out";
    private static final String outPutPath = "testForThread/data/result/";

    private static long startTime;

    private static void init() {
        startTime = System.currentTimeMillis();
        checkTempFile(tempFile);
    }

    public static void main(String[] args) {
        init();
        fillRawPage();
        fillProxies();


        GoogleThread googleThread_1 = new GoogleThread(
                60,
                rawPages,
                pages,
                proxies,
                true
        );

        GoogleThread googleThread_2 = new GoogleThread(
                60,
                rawPages,
                pages,
                proxies,
                true
        );

        GoogleThread googleThread_3 = new GoogleThread(
                60,
                rawPages,
                pages,
                proxies,
                true
        );

        Thread thread_1 = new Thread(googleThread_1);
        thread_1.start();
        Thread thread_2 = new Thread(googleThread_2);
        thread_2.start();
        Thread thread_3 = new Thread(googleThread_3);
        thread_3.start();

        System.out.println("Time work: " + Utilities.convertToTime(System.currentTimeMillis() - startTime));
    }

    private static void fillProxies() {
        try {
            proxies.put(new InetSocketAddress("62.109.8.114", 443));
            proxies.put(new InetSocketAddress("94.250.255.31", 443));
            proxies.put(new InetSocketAddress("149.154.71.37", 443));
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

        try {
            PageReader pageReader = new PageReader(tempFile);
            pages = pageReader.read();
            rawPages.addAll(pages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkTempFile(String tempFile) {

        if (! (new File(tempFile)).exists()) {
            removeWrapping(tempFile);
        }
    }

    private static void removeWrapping(String tempFile) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;

        StringBuilder cleanLine = new StringBuilder();
        String header = "";
        String line;

        try {
            bufferedReader = new BufferedReader(new FileReader(inPutPath + inPutFileName));
            header = bufferedReader.readLine();

            while ((line = bufferedReader.readLine()) != null) {
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
}
