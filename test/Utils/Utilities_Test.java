package Utils;

import com.ps.proxy.ProxyAuthenticator;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.net.Authenticator;
import java.net.InetSocketAddress;

public class Utilities_Test {
    private static final String authUser = "ZYWhsnVNY";
    private static final String authPassword = "e9kcdGk9d";

    private static void init() {
        Authenticator.setDefault(new ProxyAuthenticator(authUser, authPassword));
    }
    @Test
    public void getDocument() throws Exception {
        String host = "http://www.google.com.ua/search?q=Как%правильно%сварить%рис&num=20";
        InetSocketAddress inetSocketAddress = new InetSocketAddress(
                "62.109.8.114",
                443
        );

        Document document = Utilities.getDocument(host, inetSocketAddress);

        System.out.println(document.outerHtml());
    }

}