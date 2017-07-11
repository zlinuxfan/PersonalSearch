import Utils.Utilities;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

class ElementOfPage {
    private String name;
    private String path;
    private List<String> textBoxs = new ArrayList<>();
    private ArrayList<UrlInfo> urlInfoList;

    ElementOfPage(String name, ArrayList<UrlInfo> urlInfoList) {

        this.name = name;
        this.path = (Utilities.toTransliteration(name)).replace(" ", "-");
        this.urlInfoList = urlInfoList;
        createTextBox();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<String> getTextBoxs() {
        return textBoxs;
    }

    public ArrayList<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }

    private void createTextBox() {
        while (this.textBoxs.size() < 1) {
            for (UrlInfo urlInfo: this.urlInfoList) {
                if (!urlInfo.isYoutube()) {
                    this.textBoxs.add(getText(urlInfo.getLink().toString()));
                    this.urlInfoList.remove(urlInfo);
                }
            }
        }
    }

    private static String getText(String url) {
        Document document = Utilities.getDocument(url);
        return document != null ? document.select("div.content").text() : "";
    }
}
