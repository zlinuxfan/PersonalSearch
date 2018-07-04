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

    public UrlInfo(String source, String link, String heading, String description) {
        this.source = source;
        try {
            String normalLink = (checkUrlString(link));
            this.link = (normalLink == null || normalLink.equals("")) ? null : new URL(normalLink);
            this.youtube = this.link != null && this.link.getHost().equals("www.youtube.com");
            this.blackList = this.link != null && blackLists.contains(this.link.getHost());
        } catch (MalformedURLException e) {
            System.out.println("Do not create link from: " + link);
            e.printStackTrace();
        }

        this.description = description;
        this.heading = heading;
    }

    private String checkUrlString(String link) {
        if (link == null || link.isEmpty()) {
            return link;
        }
        String str = link.substring(0, 4);
        while (link.length() > 5 && ! str.equals("http")) {
            link = link.substring(1, link.length()-1);
            str = link.substring(0, 4);
        }
        return link.split("&")[0];
    }

    public UrlInfo(String source, String link, String heading, String description, String requestName) {
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
