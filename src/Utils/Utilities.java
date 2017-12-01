package Utils;

import com.Page;
import com.UrlInfo;
import com.ps.proxy.ProxyAuthenticator;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;


public class Utilities {
    private static final String DELIMITER = ";";
    private static final int NUMBER_ELEMENT = 5;
    private static final String NEW_LINE_SEPARATOR = "\n";

    private static final String authUser = "S4auACAuB";
    private static final String authPassword = "LxCJqdkmI";

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
                    .method(Connection.Method.GET)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.94")
                    .followRedirects(true)
                    .timeout(5000)
                    .get();
        return document;
    }

    public static Document getDocument(String host, InetSocketAddress addressProxy) throws IOException {
        Authenticator.setDefault(new ProxyAuthenticator(authUser, authPassword));

        Proxy httpProxy = new Proxy(Proxy.Type.HTTP, addressProxy);

        String response;
        StringBuilder output = new StringBuilder();

        URL url = new URL(host);
        URLConnection urlConn = url.openConnection(httpProxy);
        urlConn.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");
        urlConn.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

        while ((response = reader.readLine()) !=null) {
            output.append(response);
        }

        return  Jsoup.parse(String.valueOf(output));
    }

    public static Document getDocument(String url, String proxy, int port) throws IOException {
        Document document;

        document = Jsoup
                .connect(url)
                .proxy(proxy, port)
                .method(Connection.Method.GET)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36 OPR/42.0.2393.94")
                .followRedirects(true)
                .get();
        return document;
    }

    public static Document connectUrl(String stringURL) throws Exception {

        final String authUser = "ZYWhsnVNYa";
        final String authPassword = "e9kcdGk9d";

        System.setProperty("http.proxyHost", "149.154.71.37");
        System.setProperty("http.proxyPort", "443");
        URL url=new URL(stringURL);
        URLConnection uc = url.openConnection ();
        uc.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB;     rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");

        String encoded = Base64.getEncoder().encodeToString((authUser + ":" + authPassword).getBytes());
        uc.setRequestProperty("Proxy-Authorization", "Basic " + encoded);
        uc.connect();

        String line;
        StringBuffer tmp = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }

        in.close();
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
            System.out.println("File" + fileName + " rewrite successfully !!!");
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



    public static void writeShortHeaderInFile(String fileName) {

        FileWriter fileWriter = null;
        String[] headerElements = {"Заголовок", "Ссылка", "Описание"};
        String urls = "";

        for (int i = 2; i <= 5; i++) {
            urls += "\"" + headerElements[0] + i + "-1\"" + DELIMITER +
                    "\"" + headerElements[1] + i + "-1\"" + DELIMITER +
                    "\"" + headerElements[2] + i + "-1\"" + DELIMITER;
        }
        urls = urls.substring(0, urls.length()-1);

        try {
            fileWriter = new FileWriter(fileName);
            //Write the CSV file header
            fileWriter.append("\"GUID идентификатор элемента\"");
            fileWriter.append(DELIMITER);
            fileWriter.append("\"Название элемента\"");
            fileWriter.append(DELIMITER);
            fileWriter.append("\"Путь для раздела\"");
            fileWriter.append(DELIMITER);
            fileWriter.append("\"Адрес (youtube)\"");
            fileWriter.append(DELIMITER);
            fileWriter.append(urls);

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (Exception e) {
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

    public static void writeShortPageInFile(String fileName, Page page, boolean append) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName, append);

            fileWriter.append(addQuotes(page.getGuidOfElement()));
            fileWriter.append(DELIMITER);
            fileWriter.append(addQuotes(page.getNameOfElement()));
            fileWriter.append(DELIMITER);
            fileWriter.append(addQuotes(page.getPathlElement()));
            fileWriter.append(DELIMITER);
            fileWriter.append(addQuotes(page.getIdYouTube()));
            fileWriter.append(DELIMITER);
            fileWriter.append(urlInfoListWithoutSecondUrlToCsvOnly(page.getUrlInfoList()));
            fileWriter.append(NEW_LINE_SEPARATOR);
        } catch (Exception e) {
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

    private static String addQuotes(String line) {
        return (line == null | Objects.equals(line, "")) ? "" : String.format("\"%s\"", line);
    }

    static String urlInfoListWithoutSecondUrlToCsvOnly(List<UrlInfo> urlInfoList) {
        urlInfoList.remove(0);
        return urlInfoListToCsv(urlInfoList);
    }

    static String urlInfoListToCsv(List<UrlInfo> urlInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (UrlInfo urlInfo: urlInfoList) {
            if (urlInfo.isYoutube()) {
                continue;
            }
            stringBuilder.append(
                    String.format("\"%s\"%s\"%s\"%s\"%s\"",
                            urlInfo.getHeading(), DELIMITER,
                            urlInfo.getLink(), DELIMITER,
                            urlInfo.getDescription()
                    ))
                    .append(DELIMITER);
            if (counter == NUMBER_ELEMENT) {
                break;
            }
            counter++;
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        return stringBuilder.toString();
    }

    public static void writeStringInFile(String fileName, Page page, boolean append) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName, append);
            fileWriter.append(page.getGuidOfElement());
            fileWriter.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert fileWriter != null;
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
            }
        }
    }
}
