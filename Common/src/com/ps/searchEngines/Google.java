package com.ps.searchEngines;

import Utils.Utilities;
import com.UrlInfo;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Google implements Find {
    private static final String NAME = "google";

    private int numInRequest = 10;

    public Google(int numUrlInRequest) {
        this.numInRequest = numUrlInRequest;
    }

    public String counterSite(String requestName) throws IOException {

        String url = "http://www.google.com.ua/search?q=site%3A" + requestName + "&oq=site%3A" + requestName + "&num=" + numInRequest;

        System.out.println(url);
        Document doc = Utilities.getDocument(url);

        Elements idResults = doc.select("#resultStats");

        return idResults.text();
    }

    @Override
    public ArrayList<UrlInfo> find(String requestName, InetSocketAddress currentProxy) throws Exception {

        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "&num=" + numInRequest;
        Elements h3s;
        Elements h3Descriptions;
        //        Document doc = Utilities.getDocument(urlString);
        Document doc = Utilities.getDocument(url, currentProxy);
        ArrayList<UrlInfo> urlInfoList = new ArrayList<>();

        h3s = doc.select("h3.r a");
        h3Descriptions = doc.select("span.st");
        URL link = null;

        System.out.println("google find [" + requestName + "] ... ");

        // первые ссылки - херня, поэтому начинаем с 3
        for (int i = 3; i < h3s.size() && i < h3Descriptions.size(); i++) {
            try {
                link = new URL(Utilities.checkUrlString(h3s.get(i).select("a").first().attr("href")));

                urlInfoList.add(new UrlInfo(
                        NAME,
                        link,
                        h3s.get(i).text(),
                        h3Descriptions.get(i).text(),
                        requestName
                ));
            } catch (MalformedURLException ex) {
                System.out.println("Do not create link from: " + link);
            }
        }
        return urlInfoList;
    }

    @Override
    @Deprecated
    public ArrayList<Picture> findPicture(String requestName, int numberPicture, InetSocketAddress currentProxy) {
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "&num=" + numInRequest + "&tbm=isch";

        Document doc = null; //connectUrl(url);  //getDocument(url);
        ArrayList<Picture> pictures = new ArrayList<>();

        try {
            doc = Utilities.getDocument(url, currentProxy);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (doc != null) {
//            Elements div_jsnames = doc.select("div.rg_meta");
//
//            ObjectMapper mapper;
//            JsonNode rootNode;
//
//            for (int index = 1; index <= numberPicture; index++) {
//                mapper = new ObjectMapper();
//                try {
//                    rootNode = mapper.readValue(div_jsnames.get(index).text(), JsonNode.class);
//
//                    String urlPic = rootNode.get("ou").asText();
//
//                    pictures.add(new Picture(
//                            requestName,
//                            urlPic,
//                            rootNode.get("oh").asInt(),
//                            rootNode.get("ow").asInt(),
//                            new URL(urlPic).getProtocol()
//                    ));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        return pictures;
    }

    @Override
    public ArrayList<String> findYouTube(String requestName, int numberAds, InetSocketAddress currentProxy) throws Exception {
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "site%3Ayoutube.com&num=" + numInRequest;

        Elements h3r;
        Document doc = Utilities.getDocument(url, currentProxy); //connectUrl(url);  //getDocument(url);
        ArrayList<String> youTubeUrls = new ArrayList<>();

        h3r = doc.select("h3.r a");

        if (h3r.size() < 1) {
            return new ArrayList<>();
        }

        for (int index = 1; index <= numberAds; index++) {
            youTubeUrls.add(h3r.get(0).select("a").first().attr("href"));
        }

        return youTubeUrls;
    }
}
