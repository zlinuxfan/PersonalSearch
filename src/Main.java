import Utils.Utilities;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

//        Document document = Utilities.getDocument("http://mail.ru", new InetSocketAddress("149.154.71.37", 443));
        Document document = Utilities.getDocument("http://www.google.com", new InetSocketAddress("149.154.71.37", 443));
//        Document document = Utilities.getDocument("https://www.google.com.ua/search?q=Какой+приготовить+соус+для+риса&num=20", new InetSocketAddress("149.154.71.37", 443));
//        Document document = Utilities.getDocument("http://mail.ru");

        System.out.println(document.text());
    }
}
