import Utils.Utilities;
import com.Page;
import org.jsoup.nodes.Document;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class testForThread {
    private static BlockingQueue<ArrayList<Page>> rawPages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<ArrayList<Page>> pages = new ArrayBlockingQueue<>(3000);
    private static BlockingQueue<ArrayList<Proxy>> proxys = new ArrayBlockingQueue<>(30);

    private static void init() {
    }

    public static void main(String[] args) {
        init();
//        GoogleThread googleThread = new GoogleThread(
//                20,
//                rawPages,
//                pages,
//                true);

        String url = "https://www.google.com.ua/search?q=Как%правильно%сварить%рис&num=20";
//        String url = "https://www.google.com";

        int port = 0;
        String proxy = "";
        try {
            Document document = Utilities.connectUrl(url);
            System.out.println(document.text());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
