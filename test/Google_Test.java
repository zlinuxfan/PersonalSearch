import com.ps.Page.UrlInfo;
import com.ps.searchEngines.Google;
import com.ps.searchEngines.Picture;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class Google_Test {
    @Test
    public void find1() throws Exception {
    }

    private Google google = new Google(10);
    @Test
    public void find() throws Exception {
        ArrayList<UrlInfo> urlInfos = google.findUrls("Новогодние цвета 2015", new InetSocketAddress("localhost", 1111));

        for (UrlInfo urlInfo : urlInfos) {
            System.out.println(urlInfo.getLink());
        }
    }

    @Test
    public void findPicture() throws Exception {
        ArrayList<Picture> request = google.findPicture("Как приготовить самбук абрикосовый", 3, new InetSocketAddress("localhost", 1111));

        for (Picture picture : request) {
            System.out.println(picture.getUrl());
        }
    }

    @Test
    public void findYouTube() throws Exception {
        ArrayList<String> youTubeUrls = google.findYouTube("Как приготовить самбук абрикосовый", 3, new InetSocketAddress("localhost", 1111));

    }

}