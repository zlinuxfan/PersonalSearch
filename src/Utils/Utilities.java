package Utils;

import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Utilities {
    static {
        String log4jConfPath = "conf/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);
    }

    private static Logger log = LoggerFactory.getLogger(Utilities.class);

    public static String toTransliteration(String string) {
        final char[] ru = {'-',' ', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ч', 'Ц', 'Ш', 'Щ', 'Э', 'Ю', 'Я', 'Ы', 'Ъ', 'Ь', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ч', 'ц', 'ш', 'щ', 'э', 'ю', 'я', 'ы', 'ъ', 'ь'};
        final String[] en = {"-"," ", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "Y", "", "", "a", "b", "v", "g", "d", "e", "jo", "zh", "z", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ch", "c", "sh", "csh", "e", "ju", "ja", "y", "", ""};
        final StringBuilder response = new StringBuilder(string.length());
        final HashMap<Character, String> table = new HashMap<>();

        int i = 0;
        for (char ch : ru) {
            table.put(ch, en[i++]);
        }

        String element;

        for (i = 0; i < string.length(); i++) {
              element = table.get(string.charAt(i));
            response.append(element == null ? " " : element);
        }

        return response.toString();
    }

    public static Document getDocument(String url) throws IOException {
        Document document;

            document = Jsoup
                    .connect(url)
                    .get();
        return document;
    }

    public static Document connectUrl(String stringURL) throws Exception {
        URL url = new URL(stringURL);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("212.237.36.234", 3128)); // or whatever your proxy is
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);

        uc.connect();

        String line;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }

        return Jsoup.parse(String.valueOf(tmp));
    }

    public static void iPChange(String proxy, String port) {
        Properties systemProperties = System.getProperties();
        systemProperties.put("proxySet", "true");
        systemProperties.setProperty("http.proxyHost", proxy);
        systemProperties.setProperty("http.proxyPort", port);
    }

    public static ArrayList<String> readResource(String fileName) {
        BufferedReader fileReader = null;
        ArrayList<String> textsOfElements = new ArrayList<>();

        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {
                textsOfElements.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error in FileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
        return textsOfElements;
    }

    public static void writeResource(ArrayList<String> textsOfElements, String fileName, boolean append) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("data/" + fileName, append);
            for (String str : textsOfElements) {
                fileWriter.append(str);
                fileWriter.append("\n");
            }
            System.out.println("File" + textsOfElements + " rewrite successfully !!!");
        } catch (IOException e) {
            System.out.println("Error in CsvFileWriter_Page !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }
    }

    public static String convertToTime(long millis) {

        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
