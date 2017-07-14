import Utils.Utilities;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ElementOfPage {
    private static final int NUMBER_TEXT_BOX = 3;

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
        int counter = 1;
        while (this.textBoxes.size() <= NUMBER_TEXT_BOX) {
            for (Iterator<UrlInfo> urlInfoIterator = this.urlInfoList.iterator(); urlInfoIterator.hasNext();) {
                if (!urlInfoIterator.next().isYoutube()) {
                    this.textBoxes.add(getText(urlInfoIterator.next().getLink().toString()));
                    urlInfoIterator.remove();
                }
            }
            if (counter == this.textBoxes.size()) {
                System.out.print(" + 1");
                counter++;
            }
        }
    }

    private static String getText(String url) {
        Document document = Utilities.getDocument(url);
        return document != null ? document.select("div.content").text() : "";
    }
}
