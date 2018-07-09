package com.ps.Threads;

import com.Page;
import com.ps.searchEngines.*;
import com.UrlInfo;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;


public class PageMaker implements Runnable {

    private boolean bypass;
    private boolean running = false;
    private InetSocketAddress currentProxy;

    private BlockingQueue<Page> rawPage;
    private BlockingQueue<Page> page;
    private BlockingQueue<InetSocketAddress> proxies;
    private List<Find> searchEngines;

    private static Random random = new Random();

    public PageMaker(
            List<Find> searchEngines,
            BlockingQueue<Page> rawPage,
            BlockingQueue<Page> page,
            BlockingQueue<InetSocketAddress> proxies,
            boolean bypass) {
        this.searchEngines = searchEngines;
        this.rawPage = rawPage;
        this.page = page;
        this.proxies = proxies;
        this.bypass = bypass;
    }


    public Page fillPage(Page page) throws Exception {
        List<UrlInfo> urlInfoList = new ArrayList<>();

        for (Find searchEngine : searchEngines) {
            urlInfoList.addAll(searchEngine.find(page.getNameOfElement(), currentProxy));
        }

        if (bypass) {
            makeDelay();
        }

        if (page.getIdYouTube().isEmpty()) {
            createYouTube(page);
        }

        int index = 0;
        while (urlInfoList.size() < index || page.getUrlInfoList().size() < page.getNumberUrlInPage()) {
            if (!urlInfoList.get(index).isYoutube()) {
                page.addUrlList(urlInfoList.get(index));
            } else {
                if (page.getIdYouTube().isEmpty()) {
                    page.setPathYouTube(urlInfoList.get(index).getLink().toString());
                }
            }
            index++;
        }

        return page;
    }

    @Override
    public void run() {
        this.running = true;
        int counterError = 0;
        Page currentPage = null;
        try {
            currentPage = this.rawPage.poll(30, TimeUnit.MILLISECONDS);
            currentProxy = this.proxies.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (currentPage != null && counterError < 3) {
            try {
                this.page.put(this.fillPage(currentPage));
                currentPage = this.rawPage.poll(1100, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                counterError++;
                System.out.print("Proxy: " + currentProxy.getHostName() + "->");
                e.printStackTrace();
            }
        }

        if (counterError >= 3) {
            try {
                this.rawPage.put(currentPage);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        this.running = false;
    }

    private void createYouTube(Page page) {
        ArrayList<String> youTubes = new ArrayList<>();

        try {
            for (Find searchEngine : searchEngines) {
                youTubes.addAll(searchEngine.findYouTube(
                        page.getNameOfElement(),
                        1,
                        currentProxy
                ));
            }

            if (!youTubes.isEmpty()) {
                page.setPathYouTube(youTubes.get(0));
            }

            if (bypass) {
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
        try {
            sleep(timeOut * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }
}

