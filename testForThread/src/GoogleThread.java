import com.Page;
import com.UrlInfo;
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

import static java.lang.Thread.sleep;


class GoogleThread implements Find, Runnable { ;
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
        Document doc = getDocument(url, this.proxies.take()); //connectUrl(url);  //getDocument(url);
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
        System.out.println("start google thread.");

        while (counter++ < 3){
            try {
                this.page.put(this.find(this.rawPage.take()));
                if (bypass) {
                    makeDelay();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("---: " + counter);
        }

        this.running = false;
    }

    private static void makeDelay() {
        long timeOut = random.nextInt(57);
        System.out.println("Timeout: " + timeOut + "sec ...");
        try {
            sleep(timeOut * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    private Document getDocument(String urlStr, InetSocketAddress inetSocketAddress) throws IOException {
        Authenticator.setDefault(new ProxyAuthenticator(authUser, authPassword));

        SocketAddress addr = inetSocketAddress;
        Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addr);

        URLConnection urlConn = null;
        BufferedReader reader = null;
        String response = "";
        StringBuilder output = new StringBuilder();
        URL url= new URL(urlStr);

//Pass the Proxy instance defined above, to the openConnection() method
        urlConn = url.openConnection(httpProxy);
        urlConn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
        urlConn.connect();
        reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        response = reader.readLine();
        while (response!=null) {
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