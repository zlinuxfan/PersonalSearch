import com.ps.Page.OnceText;
import com.ps.Page.UrlInfo;

import java.net.MalformedURLException;
import java.net.URL;
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
    public void textBoxesToCsv() {

        ArrayList<OnceText> texts = new ArrayList<>();
        OnceText onceText = new OnceText("text of text box", false);
        texts.add(onceText);
        OnceText onceText1 = new OnceText("text two of text box", false);
        texts.add(onceText1);

        String s = CsvFileWriter_Page.textBoxesToCsv(texts);

        System.out.println(s);
    }

    @org.junit.Test
    public void urlInfoListToCsv() throws MalformedURLException {

        UrlInfo urlInfo = new UrlInfo("google", new URL("http://test.com"), "heading", "description");
        UrlInfo urlInfo1 = new UrlInfo("google", new URL("http://test1.com"), "heading1", "description1");
        ArrayList<UrlInfo> urlInfos = new ArrayList<>();
        urlInfos.add(urlInfo);
        urlInfos.add(urlInfo1);


        String s = CsvFileWriter_Page.urlInfoListToCsv(urlInfos);

        System.out.println(s);
    }
}