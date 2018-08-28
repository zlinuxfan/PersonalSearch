package com.ps.Threads;

import com.ps.Page.ElementOfPage;
import com.ps.Page.OnceText;
import com.ps.Page.Page;
import com.ps.Page.UrlInfo;
import com.ps.Utils.Utilities;
import com.ps.searchEngines.Find;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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


    private Page fillPage(Page page) throws Exception {
        List<UrlInfo> urlInfoList = new ArrayList<>();

        for (Find searchEngine : searchEngines) {
            urlInfoList.addAll(searchEngine.findUrls(page.getSearchQuery(), currentProxy));
        }

        createTextBoxes(page, (ArrayList<UrlInfo>) urlInfoList);

        if (bypass) {
            makeDelay();
        }

        if (page.getIdYouTube().isEmpty()) {
            createYouTube(page);
        }

        if (bypass) {
            makeDelay();
        }

        if (page.getPathImage().isEmpty()) {
            createPathImage(page);
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

    private void createTextBoxes(Page page, ArrayList<UrlInfo> urlInfoList) {
        ElementOfPage elementOfPage = new ElementOfPage("", urlInfoList);

        page.setTextBoxes(elementOfPage
                .getTextBoxes()
                .stream()
                .map(textBox -> new OnceText(textBox, true))
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    @Override
    public void run() {

        // timeout for filling the list rawPage
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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

    private void createPathImage(Page page) throws Exception {
        ArrayList<UrlInfo> urls = new ArrayList<>();

        for (Find searchEngine : searchEngines) {
            urls.addAll(searchEngine.findUrls(page.getSearchQuery() + " site:kakprosto.ru", currentProxy));
        }

        Document doc = Utilities.getDocument(urls.get(0).getLink().toString(), currentProxy);

        Elements content_img = doc.select(".content__img_wrapper");
        Element img = content_img.select("img").first();
        page.setPathImage(img.attr("abs:src"));


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

