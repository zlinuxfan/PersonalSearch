import java.net.MalformedURLException;
import java.net.URL;

public class UrlInfo {
    private URL link;
    private String heading;
    private String description;
    private String source;
    private boolean youtube;

    UrlInfo(String source, String link, String heading, String description) {
        this.source = source;
        try {
            this.link = new URL(link);
            this.youtube = this.link.getHost().equals("www.youtube.com");
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
}
