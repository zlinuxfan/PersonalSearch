package com;

import Utils.Utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

public class UrlInfo {
    private String requestName;
    private URL link;
    private String heading;
    private String description;
    private String source;
    private boolean youtube;
    private boolean blackList;

    private static HashSet<String> blackLists = getBlackList();

    public UrlInfo(String source, URL link, String heading, String description) {
        this.source = source;
        this.link = link;
        this.youtube = this.link != null && this.link.getHost().equals("www.youtube.com");
        this.blackList = this.link != null && blackLists.contains(this.link.getHost());

        this.description = description;
        this.heading = heading;
    }

    public UrlInfo(String source, URL link, String heading, String description, String requestName) {
        this(source, link, heading, description);
        this.requestName = requestName;
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

    public String getRequestName() {
        return requestName;
    }

    public boolean isBlackList() {
        return blackList;
    }

    private static HashSet<String> getBlackList() {
        ArrayList<String> list = Utilities.readResource("data/bedUrl.txt");
        return new HashSet<>(list);
    }
}
