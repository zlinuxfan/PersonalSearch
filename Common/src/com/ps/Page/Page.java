package com.ps.Page;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Page {
    private String nameParagraph; // Название раздела
    private String guidOfGroup; // GUID идентификатор группы элементов
    private String guidOfParentGroup; //GUID идентификатор родительской группы элементов
    private String sectionTitle; // Заголовок раздела(title)
    private String sectionDescription; // Описание раздела(description)
    private String sectionKeywords; // Ключевые слова раздела(keywords)
    private String briefDescriptionSection; //Описание раздела
    private String pathSection; // Путь для раздела
    private String partitionSortingSection; // Порядок сортировки раздела

    private String searchQuery = ""; //Поисковый запрос

    private String guidOfElement; // GUID идентификатор элемента
    private String nameOfElement; // название элемента
    private String briefDescriptionElement; //Описание элемента
    private String textOfElement; // Текст для элемента
    private String tags; // Метки
    private String elementActiviti; // Активность элемента
    private String sortingOrderOfElement; // Порядок сортировки элемента
    private String pathlElement; // Путь к элементу
    private String elementTitle; // Заголовок (title)
    private String elementDescription; // Значение мета-тега description для страницы с элементом
    private String elementKeywords; // Значение мета-тега keywords для страницы с элементом
    private boolean indexing; // Флаг индексации
    private String data;
    private String dataOfPublication;
    private String dataOfPublicationEnd; // Дата завершения публикации;
    private String pathImage; // Файл изображения для элемента
    private String pathImageSmall; // Файл изображения для элемента
    private String shortcuts; // Ярлыки
    private String siteUserID; // Идентификатор пользователя сайта
    private ArrayList<OnceText> textBoxes;

    private String pathYouTube;
    private String idYouTube;
    private List<UrlInfo> urlInfoList;
    private int numberUrlInPage;

    public void addUrlList(UrlInfo urlInfo) {
        this.urlInfoList.add(urlInfo);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + this.guidOfElement.hashCode() + this.searchQuery.hashCode();

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (! Page.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        Page thisObj = (Page) obj;

        return Objects.equals(this.getGuidOfElement(), thisObj.getGuidOfElement()) &&
                Objects.equals(this.getSearchQuery(), thisObj.getSearchQuery());
    }


    public static class Builder {
        private final int numberUrlInPage;
        private String nameParagraph = "";
        private String guidOfGroup = "";
        private String guidOfParentGroup = "";
        private String sectionTitle = ""; // Заголовок раздела(title)
        private String sectionDescription = ""; // Описание раздела(description)
        private String sectionKeywords = ""; // Ключевые слова раздела(keywords)
        private String briefDescriptionSection = ""; //Описание раздела
        private String pathSection = ""; // Путь для раздела

        private String searchQuery = ""; //Поисковый запрос

        private String partitionSortingSection = ""; // Порядок сортировки раздела
        private String guidOfElement = ""; // GUID идентификатор элемента
        private String nameOfElement = ""; // название элемента
        private String briefDescriptionElement = ""; //Описание элемента
        private String textOfElement = ""; // Текст для элемента
        private String tags = ""; // Метки
        private String elementActivity = "1"; // Активность элемента
        private String sortingOrderOfElement = "0"; // Порядок сортировки элемента
        private String pathElement = ""; // Путь к элементу
        private String elementTitle = ""; // Заголовок (title)
        private String elementDescription = ""; // Значение мета-тега description для страницы с элементом
        private String elementKeywords = ""; // Значение мета-тега keywords для страницы с элементом

        private boolean indexing = true; // Флаг индексации
        private String data = "02.07.2017 20:49:57";
        private String dataOfPublication = "0000-00-00 00:00:00";
        private String dataOfPublicationEnd  = "0000-00-00 00:00:00"; // Дата завершения публикации;
        private String pathImage = ""; // Файл изображения для элемента
        private String pathImageSmall = ""; // Файл изображения для элемента
        private String shortcuts = ""; // Ярлыки
        private String siteUserID = "0"; // Идентификатор пользователя сайта
        private ArrayList<OnceText> textBoxes;
        private String pathYouTube;
        private String idYouTube;

        private List<UrlInfo> urlInfoList;

        public Builder(String guidOfElement,
                       String nameOfElement,
                       String briefDescriptionElement,
                       String textOfElement,
                       String pathElement,
                       ArrayList<OnceText> textBoxes,
                       List<UrlInfo> urlInfoList,
                       int numberUrlInPage) {

            this.guidOfElement = guidOfElement;
            this.searchQuery = searchQuery;
            this.nameOfElement = nameOfElement;
            this.briefDescriptionElement = briefDescriptionElement;
            this.textOfElement = textOfElement;
            this.pathElement = pathElement;
            this.textBoxes = textBoxes;
            this.urlInfoList = urlInfoList;
            this.numberUrlInPage = numberUrlInPage;

            this.pathYouTube = selectPathYouTube(urlInfoList);
            this.idYouTube = !pathYouTube.equals("") ? crateIdYouTube(this.pathYouTube) : "";
        }

        private static String crateIdYouTube(String pathYouTube) {
            URL url = null;
            String[] split;
            try {
                url = new URL(URLDecoder.decode(checkUrlString(pathYouTube), "UTF-8"));
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
///watch%3Fv%3DUwoOfibZoYA&sa=U&ved=0ahUKEwi54Py2_OjXAhUhSJoKHUdvB_wQtwIIFzAA&usg=AOvVaw3eyJFRRnFSZWJIF
            if (url != null && url.getQuery() != null) {
                split = (url.getQuery()).split("&");
                return !split[0].isEmpty() ? split[0].substring(2) : "";
            }
            return "";
        }

        public static String checkUrlString(String pathYouTube) {

            String str = pathYouTube.substring(0, 4);

            while (! str.equals("http")) {
                pathYouTube = pathYouTube.substring(1, pathYouTube.length()-1);
                str = pathYouTube.substring(0, 4);
            }

            return pathYouTube;
        }

        private String selectPathYouTube(List<UrlInfo> urlInfoList) {
            if (urlInfoList == null) {
                return "";
            }
            for (UrlInfo urlInfo: urlInfoList) {
                if (urlInfo.isYoutube()) {
                    return urlInfo.getLink().toString();
                }
            }
            return "";
        }

        public Builder nameParagraph(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder guidOfGroup(String val) {
            this.guidOfGroup = val;
            return this;
        }

        public Builder guidOfParentGroup(String val) {
            this.guidOfParentGroup = val;
            return this;
        }

        public Builder sectionTitle(String val) {
            this.sectionTitle = val;
            return this;
        }

        public Builder sectionDescription(String val) {
            this.sectionDescription = val;
            return this;
        }

        public Builder sectionKeywords(String val) {
            this.sectionKeywords = val;
            return this;
        }

        public Builder briefDescriptionSection(String val) {
            this.briefDescriptionSection = val;
            return this;
        }

        public Builder pathSection(String val) {
            this.pathSection = val;
            return this;
        }

        public Builder partitionSortingSection(String val) {
            this.partitionSortingSection = val;
            return this;
        }

        public Builder guidOfElement(String val) {
            this.guidOfElement = val;
            return this;
        }

        public Builder tags(String val) {
            this.tags = val;
            return this;
        }

        public Builder elementActiviti(String val) {
            this.elementActivity = val;
            return this;
        }

        public Builder sortingOrderOfElement(String val) {
            this.sortingOrderOfElement = val;
            return this;
        }

        public Builder elementTitle(String val) {
            this.elementTitle = val;
            return this;
        }

        public Builder elementDescription(String val) {
            this.elementDescription = val;
            return this;
        }

        public Builder elementKeywords(String val) {
            this.elementKeywords = val;
            return this;
        }

        public Builder indexing(boolean val) {
            this.indexing = val;
            return this;
        }

        public Builder data(String val) {
            this.data = val;
            return this;
        }

        public Builder dataOfPublication(String val) {
            this.dataOfPublication = val;
            return this;
        }

        public Builder dataOfPublicationEnd(String val) {
            this.dataOfPublicationEnd = val;
            return this;
        }

        public Builder pathImage(String val) {
            this.pathImage = val;
            return this;
        }

        public Builder pathImageSmall(String val) {
            this.pathImageSmall = val;
            return this;
        }

        public Builder shortcuts(String val) {
            this.shortcuts = val;
            return this;
        }

        public Builder siteUserID(String val) {
            this.siteUserID = val;
            return this;
        }

        public Builder patchYouTube(String patch) {
            this.pathYouTube = patch;
            return this;
        }

        public Builder idYouTube(String idYouTube) {
            this.idYouTube = idYouTube;
            return this;
        }

        public Builder searchQuery(String searchQuery) {
            this.searchQuery = searchQuery;
            return this;
        }

        public Page build() {
            return new Page(this);
        }
    }

    public Page(Builder builder) {
        this.nameParagraph = builder.nameParagraph;
        this.guidOfGroup = builder.guidOfGroup;
        this.guidOfParentGroup = builder.guidOfParentGroup;
        this.sectionTitle = builder.sectionTitle;
        this.sectionDescription = builder.sectionDescription;
        this.sectionKeywords = builder.sectionKeywords;
        this.briefDescriptionSection = builder.briefDescriptionSection;
        this.pathSection = builder.pathSection;
        this.partitionSortingSection = builder.partitionSortingSection;
        this.guidOfElement = builder.guidOfElement;
        this.nameOfElement = builder.nameOfElement;
        this.briefDescriptionElement = builder.briefDescriptionElement;
        this.textOfElement = builder.textOfElement;
        this.tags = builder.tags;
        this.elementActiviti = builder.elementActivity;
        this.sortingOrderOfElement = builder.sortingOrderOfElement;
        this.pathlElement = builder.pathElement;
        this.elementTitle = builder.elementTitle;
        this.elementDescription = builder.elementDescription;
        this.elementKeywords = builder.elementKeywords;
        this.indexing = builder.indexing;
        this.data = builder.data;
        this.dataOfPublication = builder.dataOfPublication;
        this.dataOfPublicationEnd = builder.dataOfPublicationEnd;
        this.pathImage = builder.pathImage;
        this.pathImageSmall = builder.pathImageSmall;
        this.shortcuts = builder.shortcuts;
        this.siteUserID = builder.siteUserID;
        this.textBoxes = builder.textBoxes;
        this.pathYouTube = builder.pathYouTube;
        this.idYouTube = builder.idYouTube;
        this.urlInfoList = builder.urlInfoList;
        this.numberUrlInPage = builder.numberUrlInPage;
        this.searchQuery = builder.searchQuery;
    }

    public String getNameParagraph() {
        return nameParagraph;
    }

    public String getGuidOfGroup() {
        return guidOfGroup;
    }

    public String getGuidOfParentGroup() {
        return guidOfParentGroup;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public String getSectionKeywords() {
        return sectionKeywords;
    }

    public String getBriefDescriptionSection() {
        return briefDescriptionSection;
    }

    public String getPathSection() {
        return pathSection;
    }

    public String getPartitionSortingSection() {
        return partitionSortingSection;
    }

    public String getGuidOfElement() {
        return guidOfElement;
    }

    public String getNameOfElement() {
        return nameOfElement;
    }

    public String getBriefDescriptionElement() {
        return briefDescriptionElement;
    }

    public String getTextOfElement() {
        return textOfElement;
    }

    public String getTags() {
        return tags;
    }

    public String getElementActiviti() {
        return elementActiviti;
    }

    public String getSortingOrderOfElement() {
        return sortingOrderOfElement;
    }

    public String getPathlElement() {
        return pathlElement;
    }

    public String getElementTitle() {
        return elementTitle;
    }

    public String getElementDescription() {
        return elementDescription;
    }

    public String getElementKeywords() {
        return elementKeywords;
    }

    public boolean isIndexing() {
        return indexing;
    }

    public String getData() {
        return data;
    }

    public String getDataOfPublication() {
        return dataOfPublication;
    }

    public String getDataOfPublicationEnd() {
        return dataOfPublicationEnd;
    }

    public String getPathImage() {
        return pathImage;
    }

    public String getPathImageSmall() {
        return pathImageSmall;
    }

    public String getShortcuts() {
        return shortcuts;
    }

    public String getSiteUserID() {
        return siteUserID;
    }

    public ArrayList<OnceText> getTextBoxes() {
        return textBoxes;
    }

    public List<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }

    public String getIdYouTube() {
        return idYouTube;
    }

    public void setIndexing(boolean indexing) {
        this.indexing = indexing;
    }

    public void setPathYouTube(String pathYouTube) {
        this.pathYouTube = pathYouTube;
        this.idYouTube = !pathYouTube.equals("") ? Builder.crateIdYouTube(this.pathYouTube) : "";
    }

    public int getNumberUrlInPage() {
        return numberUrlInPage;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setTextBoxes(ArrayList<OnceText> textBoxes) {
        this.textBoxes = textBoxes;
    }
}
