package com.ps.Threads;

import Utils.Utilities;
import com.Page;
import com.UrlInfo;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

//TODO rename to PageMaker
public class GoogleThread implements com.ps.Threads.Find, Runnable {

    private int numInRequest;
    private boolean bypass;
    private static final String NAME = "google";
    private boolean running = false;
    private InetSocketAddress currentProxy;

    private BlockingQueue<Page> rawPage;
    private BlockingQueue<Page> page;
    private BlockingQueue<InetSocketAddress> proxies;

    private static Random random = new Random();

    public GoogleThread(
            int numUrlInRequest,
            BlockingQueue<Page> rawPage,
            BlockingQueue<Page> page,
            BlockingQueue<InetSocketAddress> proxies,
            boolean bypass) {
        this.numInRequest = numUrlInRequest;
        this.rawPage = rawPage;
        this.page = page;
        this.proxies = proxies;
        this.bypass = bypass;
    }

    @Override
    public Page find(Page page) throws Exception {
        String urlString = "http://www.google.com.ua/search?q=" + page.getNameOfElement().replace(" ", "+") + "&num=" + numInRequest;
        Elements h3s;
        Elements h3Descriptions;
//        Document doc = Utilities.getDocument(urlString);
        Document doc = Utilities.getDocument(urlString, currentProxy);
        ArrayList<UrlInfo> urlInfoList = new ArrayList<>();

        h3s = doc.select("h3.r a");
        h3Descriptions = doc.select("span.st");
        URL url = null;

        System.out.println("google find [" + page.getNameOfElement() + "] ... ");

        for (int i = 0; i < h3s.size() && i < h3Descriptions.size(); i++) {
            try {
                url = new URL(Utilities.checkUrlString(h3s.get(i).select("a").first().attr("href")));
                urlInfoList.add(new UrlInfo(
                        NAME,
                        url,
                        h3s.get(i).text(),
                        h3Descriptions.get(i).text(),
                        page.getNameOfElement()
                ));
            } catch (MalformedURLException ex) {
                System.out.println("Do not create link from: " + url);
            }
        }

        int index = 0;
        while (urlInfoList.size() < index || page.getUrlInfoList().size() < 5) {
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
                currentPage = this.find(currentPage);
                if (currentPage.getIdYouTube().isEmpty()) {
                    createYouTube(currentPage);
                }
                this.page.put(currentPage);
                currentPage = this.rawPage.poll(1100, TimeUnit.MILLISECONDS);
                if (bypass) {
                    makeDelay();
                }
            } catch (Exception e) {
                counterError++;
                System.out.println("Proxy: " + currentProxy.getHostName() + "->");
                if (currentPage != null) {
                    try {
                        this.rawPage.put(currentPage);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }

        this.running = false;
    }

    private void createYouTube(Page page) {
        try {
            ArrayList<String> youTubes = this.findYouTube(
                    page.getNameOfElement(),
                    1,
                    10
            );

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

    private ArrayList<String> findYouTube(String requestName, int numberAds, int numberRequestInPage) throws Exception {
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "site%3Ayoutube.com&num=" + numberRequestInPage;

        Elements h3r;
//        Document doc = Utilities.getDocument(url);
        Document doc = Utilities.getDocument(url, currentProxy);
        ArrayList<String> youTubeUrls = new ArrayList<>();

        h3r = doc.select("h3.r a");

        if (h3r.size() < 1) {
            return youTubeUrls;
        }

        for (int index = 1; index <= numberAds; index++) {
            youTubeUrls.add(h3r.get(0).select("a").first().attr("href"));
        }

        return youTubeUrls;
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

