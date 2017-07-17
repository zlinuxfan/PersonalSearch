package Utils;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

public class Utilities {
    static {
        String log4jConfPath = "conf/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    private static Logger log = LoggerFactory.getLogger(Utilities.class);

    public static String toTransliteration(String string) {
        final char[] ru = {' ', 'А','Б','В','Г','Д','Е','Ё', 'Ж', 'З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ч', 'Ц','Ш', 'Щ', 'Э','Ю', 'Я', 'Ы','Ъ', 'Ь', 'а','б','в','г','д','е','ё', 'ж', 'з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ч', 'ц','ш', 'щ', 'э','ю', 'я', 'ы','ъ','ь'};
        final String[] en = {" ", "A","B","V","G","D","E","Jo","Zh","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","H","Ch","C","Sh","Csh","E","Ju","Ja","Y","", "", "a","b","v","g","d","e","jo","zh","z","i","j","k","l","m","n","o","p","r","s","t","u","f","h","ch","c","sh","csh","e","ju","ja","y","",""};
        final StringBuilder response = new StringBuilder(string.length());
        final HashMap<Character, String> table = new HashMap<>();

        int i = 0;
        for (char ch: ru) {
            table.put(ch, en[i++]);
        }

        for (i = 0; i < string.length(); i++) {
            response.append(table.get(string.charAt(i)));
        }

        return response.toString();
    }

    public static Document getDocument(String url) {
        Document document = null;

//        System.setProperty("http.proxyHost", "176.104.218.40");
//        System.setProperty("http.proxyPort", "8081");
//        System.setProperty("http.proxyHost", "159.203.63.183");
//        System.setProperty("http.proxyPort", "3128");
//        System.setProperty("http.proxyHost", "95.80.98.41");
//        System.setProperty("http.proxyPort", "8080");

//        Proxy proxy = new Proxy(
//                Proxy.Type.HTTP,
//                InetSocketAddress.createUnresolved("178.22.148.122", 3129)
//        );

        try {
            document = Jsoup
                    .connect(url)
//                    .proxy(proxy)
//                    .userAgent("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2") //
//                    .header("Content-Language", "en-US") //
                    .get();
        } catch (IOException e) {
            log.error("Jsoup.connect error: " + url);
        }

        return document;
    }

    public static void main(String[] args) {

        String src = "Как сделать бумажный меч";

        System.out.println(toTransliteration(src));
    }
}
