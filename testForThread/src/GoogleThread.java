import com.Page;
import com.UrlInfo;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;


class GoogleThread implements Find, Runnable {
    ;
    private int numInRequest;
    private boolean bypass = false;
    private static final String NAME = "google";
    private boolean running = false;

    private static final String authUser = "ZYWhsnVNY";
    private static final String authPassword = "e9kcdGk9d";

    private BlockingQueue<Page> rawPage = null;
    private BlockingQueue<Page> page = null;
    private BlockingQueue<InetSocketAddress> proxies = null;

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
        String url = "https://www.google.com.ua/search?q=" + page.getNameOfElement().replace(" ", "+") + "&num=" + numInRequest;
        Elements h3s;
        Elements h3Descriptions;
        Document doc = getDocument(url); //getDocument(url, this.proxies.take()); //connectUrl(url);  //getDocument(url);
        ArrayList<UrlInfo> urlInfoList = new ArrayList<>();

        h3s = doc.select("h3.r a");
        h3Descriptions = doc.select("span.st");

        System.out.println("google find [" + page.getNameOfElement() + "] ... ");

        for (int i = 0; i < h3s.size() && i < h3Descriptions.size(); i++) {
            urlInfoList.add(new UrlInfo(
                    NAME,
                    h3s.get(i).select("a").first().attr("abs:href"),
                    h3s.get(i).text(),
                    h3Descriptions.get(i).text(),
                    page.getNameOfElement()
            ));
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
        int counter = 0;
        this.running = true;
        Page currentPage = null;
        try {
            currentPage = this.rawPage.poll(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (currentPage != null && counter++ < 3) {
            try {
                currentPage = this.find(currentPage);
                if (currentPage.getIdYouTube().isEmpty()) {
                    System.out.println("youtube is empty ...");
                    createYouTube(currentPage);
                }
                this.page.put(currentPage);
                currentPage = this.rawPage.poll(3000, TimeUnit.MILLISECONDS);
                if (bypass) {
                    makeDelay();
                }
            } catch (Exception e) {
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
        Document doc = getDocument(url);
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
        long timeOut = random.nextInt(37);
        try {
            sleep(timeOut * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    private Document getDocument(String url) throws IOException {
        Document document;

        document = Jsoup
                .connect(url)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.94")
                .followRedirects(true)
                .timeout(5000)
                .get();
        return document;
    }

    private Document getDocument(String urlStr, InetSocketAddress inetSocketAddress) throws IOException {
        Authenticator.setDefault(new ProxyAuthenticator(authUser, authPassword));

        SocketAddress addr = inetSocketAddress;
        Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);

        URLConnection urlConn = null;
        BufferedReader reader = null;
        String response = "";
        StringBuilder output = new StringBuilder();
        URL url = new URL(urlStr);

//Pass the Proxy instance defined above, to the openConnection() method
        urlConn = url.openConnection(httpProxy);
        urlConn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
        urlConn.connect();
        reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        response = reader.readLine();
        while (response != null) {
            output.append(response);
            response = reader.readLine();
        }
        return Jsoup.parse(String.valueOf(output));
    }
}

class ProxyAuthenticator extends Authenticator {

    private String user, password;

    public ProxyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }
}