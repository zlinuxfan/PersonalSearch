import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

public class Google_Test {
    private Google google = new Google();
    @Test
    public void find() throws Exception {
        ArrayList<UrlInfo> urlInfos = google.find("Как приготовить самбук абрикосовый");

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
    public void findYouTube() throws IOException {
        ArrayList<String> youTubeUrls = google.findYouTube("Как приготовить самбук абрикосовый", 3, 10);

    }

}