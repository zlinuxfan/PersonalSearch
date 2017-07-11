import java.util.ArrayList;
import java.util.List;

public class Page {
    private String nameParagraph;
    private String guidOfGroup;
    private String guidOfParentGroup;
    private String sectionTitle; // Заголовок раздела(title)
    private String sectionDescription; // Описание раздела(description)
    private String[] sectionKeywords; // Ключевые слова раздела(keywords)
    private String briefDescriptionSection; //Описание раздела
    private String pathSection; // Путь для раздела
    private String partitionSortingSection; // Порядок сортировки раздела

    private String guidOfElement; // GUID идентификатор элемента
    private String nameOfElement; // название элемента
    private String briefDescriptionElement; //Описание элемента
    private String textOfElement; // Текст для элемента
    private String tags; // Метки
    private String elementActiviti; // Активность элемента
    private String sortingOrderOfElement; // Порядок сортировки элемента
    private String pathlElement; // Путь к элементукr
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
    private ArrayList<OnceText> textBoxs;
    private String pathYouTube;
    private List<UrlInfo> urlInfoList;

    public static class Builder {
        private String nameParagraph = "default";
        private String guidOfGroup = "ACEE8C4B-D51D-493C-151B-C36F6A98600E";
        private String guidOfParentGroup = "ID00000000";
        private String sectionTitle; // Заголовок раздела(title)
        private String sectionDescription; // Описание раздела(description)
        private String[] sectionKeywords; // Ключевые слова раздела(keywords)
        private String briefDescriptionSection; //Описание раздела
        private String pathSection = "default-group"; // Путь для раздела
        private String partitionSortingSection; // Порядок сортировки раздела

        private String guidOfElement; // GUID идентификатор элемента
        private String nameOfElement; // название элемента
        private String briefDescriptionElement; //Описание элемента
        private String textOfElement; // Текст для элемента
        private String tags; // Метки
        private String elementActiviti = "1"; // Активность элемента
        private String sortingOrderOfElement = "0"; // Порядок сортировки элемента
        private String pathElement; // Путь к элементу
        private String elementTitle; // Заголовок (title)
        private String elementDescription; // Значение мета-тега description для страницы с элементом
        private String elementKeywords; // Значение мета-тега keywords для страницы с элементом
        private boolean indexing = true; // Флаг индексации

        private String data = "02.07.2017 20:49:57";
        private String dataOfPublication = "0000-00-00 00:00:00";
        private String dataOfPublicationEnd  = "0000-00-00 00:00:00"; // Дата завершения публикации;
        private String pathImage; // Файл изображения для элемента
        private String pathImageSmall; // Файл изображения для элемента
        private String shortcuts; // Ярлыки
        private String siteUserID = "0"; // Идентификатор пользователя сайта
        private ArrayList<OnceText> textBoxs;
        private String pathYouTube;
        private List<UrlInfo> urlInfoList;

        public Builder(String guidOfElement,
                       String nameOfElement,
                       String briefDescriptionElement,
                       String textOfElement,
                       String pathElement,
                       ArrayList<OnceText> textBoxs,
                       String pathYouTube,
                       List<UrlInfo> urlInfoList) {

            this.guidOfElement = guidOfElement;
            this.nameOfElement = nameOfElement;
            this.briefDescriptionElement = briefDescriptionElement;
            this.textOfElement = textOfElement;
            this.pathElement = pathElement;
            this.textBoxs = textBoxs;
            this.pathYouTube = pathYouTube;
            this.urlInfoList = urlInfoList;
        }

        public Builder nameParagraph(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder guidOfGroup(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder guidOfParentGroup(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder sectionTitle(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder sectionDescription(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder sectionKeywords(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder briefDescriptionSection(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder pathSection(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder partitionSortingSection(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder guidOfElement(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder tags(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder elementActiviti(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder sortingOrderOfElement(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder elementTitle(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder elementDescription(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder elementKeywords(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder indexing(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder data(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder dataOfPublication(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder dataOfPublicationEnd(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder pathImage(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder pathImageSmall(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder shortcuts(String val) {
            this.nameParagraph = val;
            return this;
        }

        public Builder siteUserID(String val) {
            this.nameParagraph = val;
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
        this.elementActiviti = builder.elementActiviti;
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
        this.textBoxs = builder.textBoxs;
        this.pathYouTube = builder.pathYouTube;
        this.urlInfoList = builder.urlInfoList;
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

    public String[] getSectionKeywords() {
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

    public ArrayList<OnceText> getTextBoxs() {
        return textBoxs;
    }

    public String getPathYouTube() {
        return pathYouTube;
    }

    public List<UrlInfo> getUrlInfoList() {
        return urlInfoList;
    }
}
