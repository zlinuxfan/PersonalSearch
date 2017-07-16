import Utils.Utilities;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class ElementOfPage {
    private static final int NUMBER_TEXT_BOX = 3;
    private static Logger log = LoggerFactory.getLogger(ElementOfPage.class);

    private String name;
    private String path;
    private List<String> textBoxes = new ArrayList<>();
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

    public List<String> getTextBoxes() {
        return textBoxes;
    }

    public ArrayList<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }

    private void createTextBox() {
        System.out.println("Crate text boxes ...");
        Iterator<UrlInfo> urlInfoIterator = this.urlInfoList.iterator();
        String text;

        while (this.textBoxes.size() <= NUMBER_TEXT_BOX && urlInfoIterator.hasNext()) {
            try {
                    if (!urlInfoIterator.next().isYoutube()) {
                        text = getText(urlInfoIterator.next().getLink().toString());
                        if (!text.equals("")) {
                            this.textBoxes.add(text);
                            urlInfoIterator.remove();
                        }

                    }
            } catch (NoSuchElementException e) {
                log.error("java.util.NoSuchElementException.");
            }
        }
    }

    private static String getText(String url) {
        Document document = Utilities.getDocument(url);
        return document != null ? document.select("div.content").text() : "";
    }
}
