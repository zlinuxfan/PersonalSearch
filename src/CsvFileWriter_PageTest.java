import java.util.ArrayList;

public class CsvFileWriter_PageTest {

    @org.junit.Test
    public void crateIframe() {

        String s = CsvFileWriter_Page.crateIframe(100, 150, "hhtp:/youtube/watch", 0);

        System.out.println(s);

    }

    @org.junit.Test
    public void crateIframe_if_src_isNull() {

        String s = CsvFileWriter_Page.crateIframe(100, 150, "", 0);

        System.out.println(s);

    }

    @org.junit.Test
    public void urlInfoListToCsv() {

        UrlInfo urlInfo = new UrlInfo("google", "http://test.com", "heading", "description");
        UrlInfo urlInfo1 = new UrlInfo("google", "http://test1.com", "heading1", "description1");
        ArrayList<UrlInfo> urlInfos = new ArrayList<>();
        urlInfos.add(urlInfo);
        urlInfos.add(urlInfo1);


        String s = CsvFileWriter_Page.urlInfoListToCsv(urlInfos);

        System.out.println(s);
    }
}