import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

public class UrlInfo {
    private URL link;
    private String heading;
    private String description;
    private String source;
    private boolean youtube;

    UrlInfo(String source, String link, String heading, String description) {
        this.source = source;
        try {
            this.link = link.equals("") ? null : new URL(link);
            this.youtube = this.link != null && this.link.getHost().equals("www.youtube.com");
        } catch (MalformedURLException e) {
            System.out.println("Do not create link from: " + link);
            e.printStackTrace();
        }

        this.description = description;
        this.heading = heading;
    }

    public URL getLink() {
        return link;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public boolean isYoutube() {
        return youtube;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        UrlInfo urlInfo = new UrlInfo("", "https://www.youtube.com/watch?v=yx95cFI9kZU", "", "");

        System.out.println(urlInfo.getLink().getQuery());

        String[] pairs = urlInfo.getLink().getQuery().split("\\?");

        System.out.println(URLDecoder.decode(pairs[0].substring(2), "UTF-8"));

    }
}
