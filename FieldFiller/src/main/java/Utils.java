import com.ps.Page.Page;
import com.ps.Page.PageReader;
import com.ps.Threads.PageMaker;
import com.ps.Utils.Utilities;
import com.ps.searchEngines.Find;
import com.ps.searchEngines.Google;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Utils {
    static ArrayList<Page> readPagesInF(String fileName, int numberUrlInPageFile) {
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
                            numberUrlInPageFile
                    )
                            .searchQuery(searchQuery)
                            .build());
        }

        return pages;
    }

    static void fillProxies(String fileName, BlockingQueue<InetSocketAddress> proxies) {
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

    static void removeWrapping(String tempFile, String srcFile) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;


        StringBuilder cleanLine = new StringBuilder();
        String line;
        String header = "";

        try {
            bufferedReader = new BufferedReader(new FileReader(srcFile));
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

    static void printPage(Page page) {
        System.out.println(String.format("%s, %s, %s",
                page.getSearchQuery(),
                page.getNameOfElement(),
                page.getElementTitle()
        ));
    }

    static void fillRawPage(
            BlockingQueue<Page> rawPages,
            String tmpSrcFile,
            String tmpCompletedQuery,
            String outPutFile,
            int numberUrlInPageFile,
            String headerOutput,
            String headerTemp) {
        ArrayList<Page> pages;
        ArrayList<Page> downPages;

        try {
            PageReader pageReader = new PageReader(tmpSrcFile, numberUrlInPageFile);
            pages = pageReader.read();
            rawPages.addAll(pages);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (new File(outPutFile).exists() &&
                new File(tmpCompletedQuery).exists()) {
            downPages = Utils.readPagesInF(tmpCompletedQuery, numberUrlInPageFile);
            rawPages.removeAll(downPages);
        } else {
            if ( new File(tmpCompletedQuery).exists()) {
                Utilities.writeStringToFile(tmpCompletedQuery, headerTemp, true);
            }
        }
    }

    static void createProxyThreadPool(
            PageMaker[] pageMakers,
            BlockingQueue<Page> rawPages,
            BlockingQueue<Page> pages,
            BlockingQueue<InetSocketAddress> proxies,
            boolean bypass
            ) {

        ArrayList<Find> searchEngines = new ArrayList<>();
        searchEngines.add(new Google(30));

        for (int i = 0; i < pageMakers.length; i++) {
            pageMakers[i] = new PageMaker(
                    searchEngines,
                    rawPages,
                    pages,
                    proxies,
                    bypass
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
