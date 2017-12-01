import Utils.Utilities;
import com.ps.proxy.ProxyAuthenticator;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;

public class Main {
    private static final String authUser = "S4auACAuB";
    private static final String authPassword = "LxCJqdkmI";

    public static void main(String[] args) throws IOException {
        Authenticator.setDefault(new ProxyAuthenticator(authUser, authPassword));

        Document document = Utilities.getDocument("http://mail.ru", new InetSocketAddress("149.154.71.37", 443));

        System.out.println(document.text());
    }
}
