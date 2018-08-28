package com.ps.Page;

import com.ps.Utils.Utilities;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ElementOfPage {
    private static final int NUMBER_TEXT_BOX = 2;
    private static Logger log = LoggerFactory.getLogger(ElementOfPage.class);

    private String name;
    private String path;
    private List<String> textBoxes = new ArrayList<>();
    private ArrayList<UrlInfo> urlInfoList;

    static private ArrayList<String> bedUrls = new ArrayList<>();

    public ElementOfPage(String name, ArrayList<UrlInfo> urlInfoList) {

        this.name = name;
        this.path = (Utilities.toTransliteration(name)).replace(" ", "-");
        this.urlInfoList = urlInfoList;
        createTextBox();
    }

    public static ArrayList<String> getBedUrls() {
        return bedUrls;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<String> getTextBoxes() {
        return textBoxes;
    }

    public ArrayList<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }

    private void createTextBox() {
        System.out.println("Crate text boxes ... ");
        Iterator<UrlInfo> urlInfoIterator = this.urlInfoList.iterator();
        String text;
        int counter = 0;

        while (this.textBoxes.size() < NUMBER_TEXT_BOX && urlInfoIterator.hasNext()) {
            try {
                UrlInfo next = urlInfoIterator.next();
                if (!next.isYoutube() && !next.isBlackList()) {
                        text = ((getText(next.getLink().toString()))
                                .replace("\n", ""))
                                .replace("\r", "")
                                .replace(";", "")
                                .replace("\"", "\"\"");
                        if (!text.equals("") && text.length() < 7_000) {
                            this.textBoxes.add(text);
                            urlInfoIterator.remove();
                            //System.out.print(" [" + text.length() + "] ");
                        }
                    }
                //System.out.print(this.textBoxes.size()+ " > ");
            } catch (NoSuchElementException e) {
                log.error("java.util.NoSuchElementException.");
            }
            counter++;
        }
        //System.out.println("Read " + counter);
    }

    private static String getText(String url) {
        Document document = null;
        URL bedUrl;
        try {
            document = Utilities.getDocument(url);
        } catch (IOException e) {
            log.error("    error: \"" + url + "\" сan not be processed.");
            try {
                bedUrl = new URL(url);
                if (bedUrl.getHost() != null) {
                    bedUrls.add(bedUrl.getHost());
                }
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        }
        return document != null ? document.select("div.content").text() : "";
    }
}
