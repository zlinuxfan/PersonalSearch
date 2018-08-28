import com.ps.Page.Page;
import com.ps.Threads.PageMaker;
import com.ps.Utils.Utilities;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FF_additional_run {
    private static BlockingQueue<Page> rawPages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<Page> pages = new ArrayBlockingQueue<>(1000);
    private static BlockingQueue<InetSocketAddress> proxies = new ArrayBlockingQueue<>(50);

    private static PageMaker[] pageMakers = new PageMaker[8];

    private static final String tempPath = "FieldFiller/data/tmp/";
    private static final String tempFile = "temp.csv";
    private static final String proxyFile = "Common/data/proxy-9.txt";
    private static final String tmpCompletedQuery = "temp.completedQuery.csv";
    private static final String inPutFileName = "топ 500 +++ (Большие буквы)500.csv";
    private static final String inPutPath = "FieldFiller/data/src/";
    private static final String outPutFileName = inPutFileName  + ".out";
    private static final String outPutFileName_textBoxes = inPutFileName + ".textBoxes.out";
    private static final String outPutPath = "FieldFiller/data/result/";
    private static final int NUMBER_URL_IN_PAGE = 4;
    private static int numberOfPage;
    private static final String headerOutput = "Название элемента;Заголовок (title);Файл изображения для элемента;Адрес (youtube);Заголовок1-1;Ссылка1-1;Описание1-1;Заголовок2-1;Ссылка2-1;Описание2-1;Заголовок3-1;Ссылка3-1;Описание3-1;Заголовок4-1;Ссылка4-1;Описание4-1";
    private static final String headerOutput_textBoxes = "Название элемента;Заголовок (title);Форматированное текстовое поле1-1;Чек бокс1-1;Форматированное текстовое поле2-1;Чек бокс2-1";
    private static final String headerTemp = "Поисковый запрос";

    private static long startTime;

    public static void main(String[] args) {
        Page page;

        int counterPage = 0;
        init();
        Utils.createProxyThreadPool(
                pageMakers,
                rawPages,
                pages,
                proxies,
                true);

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
                        Utilities.writeShortPageToFile(outPutPath + outPutFileName, page, true);
                        Utilities.writeTextBoxesToFile(outPutPath + outPutFileName_textBoxes, page, true);
                        Utilities.writeStringToFile(tempPath+ tmpCompletedQuery, page.getSearchQuery(), true);
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

    private static void init() {
        startTime = System.currentTimeMillis();
        checkTempFile(tempPath+tempFile);

        if (!new File(outPutPath + outPutFileName).exists()) {
            Utilities.writeStringToFile(outPutPath + outPutFileName, headerOutput, true);
        }

        if (!new File(outPutPath + outPutFileName_textBoxes).exists()) {
            Utilities.writeStringToFile(outPutPath + outPutFileName_textBoxes, headerOutput_textBoxes, true);
        }

        Utils.fillRawPage(
                rawPages,
                tempPath+tempFile,
                tempPath+ tmpCompletedQuery,
                outPutPath+outPutFileName,
                NUMBER_URL_IN_PAGE,
                headerOutput,
                headerTemp
        );
        Utils.fillProxies(proxyFile, proxies);

        numberOfPage = rawPages.size();
    }

    private static void checkTempFile(String tempFile) {

        if (!(new File(tempFile)).exists()) {
            Utils.removeWrapping(tempFile, inPutPath + inPutFileName);
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
