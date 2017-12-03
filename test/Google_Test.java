import com.UrlInfo;
import org.junit.Test;

import java.util.ArrayList;

public class Google_Test {
    @Test
    public void find1() throws Exception {
    }

    private Google google = new Google();
    @Test
    public void find() throws Exception {
        ArrayList<UrlInfo> urlInfos = google.find("Новогодние цвета 2015");

        for (UrlInfo urlInfo : urlInfos) {
            System.out.println(urlInfo.getLink());
        }
    }

    @Test
    public void findPicture() throws Exception {
        ArrayList<Picture> request = google.findPicture("Как приготовить самбук абрикосовый", 3);

        for (Picture picture : request) {
            System.out.println(picture.getUrl());
        }
    }

    @Test
    public void findYouTube() throws Exception {
        ArrayList<String> youTubeUrls = google.findYouTube("Как приготовить самбук абрикосовый", 3, 10);

    }

}