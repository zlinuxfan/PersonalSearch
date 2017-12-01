import Utils.*;
import com.UrlInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;

class Google implements Find {
    private static final int NUM_IN_REQUEST = 15;
    private static final String NAME = "google";
    private static final InetSocketAddress inetSocketAddress = new InetSocketAddress(
            "62.109.8.114",
            443
    );

    @Override
    public ArrayList<UrlInfo> find(String requestName) throws Exception {


        //&tbm=isch
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "&num=" + NUM_IN_REQUEST;
        Elements h3s;
        Elements h3Descriptions;
        Document doc = Utilities.getDocument(url, inetSocketAddress); //connectUrl(url);  //getDocument(url);
        ArrayList<UrlInfo> urlInfoList = new ArrayList<>();

        h3s = doc.select("h3.r a");
        h3Descriptions = doc.select("span.st");

        for (int i = 0; i < h3s.size() && i < h3Descriptions.size(); i++) {
            urlInfoList.add(new UrlInfo(
                    NAME,
                    h3s.get(i).select("a").first().attr("abs:href"),
                    h3s.get(i).text(),
                    h3Descriptions.get(i).text()
            ));
        }
        System.out.println("google find ...");
        return urlInfoList;
    }

    /*
    * <div jsnames="x.z" class="rg_meta"> ... </div>
    * {"cb":18,"cl":18,"clt":"n","cr":18,"id":"WfncJqEO5J_BTM:","isu":"youtube.com","itg":0,"ity":"jpg",
    *
    * "oh":720,
    * "ou":"https://i.ytimg.com/vi/b34HTfC-RZ8/maxresdefault.jpg",
    * "ow":1280,
    *
    * "pt":"Самбук абрикосовый. - YouTube","rid":"-ZefWrD1-hYtLM","rmt":0,"rt":0,"ru":"https://www.youtube.com/watch?v\u003db34HTfC-RZ8","s":"","st":"YouTube","th":168,"tu":"https://encrypted-tbn0.gstatic.com/images?q\u003dtbn:ANd9GcQxfefauqsg95SbMTSweqlLa6nWI8zdgqmhApvitJrxDSAsEcAN","tw":300}
     */

    public ArrayList<Picture> findPicture(String requestName, int numberPicture) {
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "&num=" + NUM_IN_REQUEST + "&tbm=isch";

        Document doc = null; //connectUrl(url);  //getDocument(url);
        ArrayList<Picture> pictures = new ArrayList<>();

        try {
            doc = Utilities.getDocument(url, inetSocketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (doc != null) {
            Elements div_jsnames = doc.select("div.rg_meta");

            ObjectMapper mapper;
            JsonNode rootNode;

            for (int index = 1; index <= numberPicture; index++) {
                mapper = new ObjectMapper();
                try {
                    rootNode = mapper.readValue(div_jsnames.get(index).text(), JsonNode.class);

                    String urlPic = rootNode.get("ou").asText();

                    pictures.add(new Picture(
                            requestName,
                            urlPic,
                            rootNode.get("oh").asInt(),
                            rootNode.get("ow").asInt(),
                            new URL(urlPic).getProtocol()
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pictures;
    }

    public ArrayList<String> findYouTube(String requestName, int numberAds, int numberRequestInPage) throws Exception {
        String url = "http://www.google.com.ua/search?q=" + requestName.replace(" ", "+") + "site%3Ayoutube.com&num=" + numberRequestInPage ;

        Elements h3r;
        Document doc = Utilities.getDocument(url, inetSocketAddress); //connectUrl(url);  //getDocument(url);
        ArrayList<String> youTubeUrls = new ArrayList<>();

        h3r = doc.select("h3.r a");

        if (h3r.size() < 1) {
            return youTubeUrls;
        }

        for (int index = 1; index <= numberAds; index++) {
            youTubeUrls.add(h3r.get(0).select("a").first().attr("href"));
        }

        return youTubeUrls;
    }
}
