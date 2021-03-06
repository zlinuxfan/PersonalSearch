public class Resource {
    private String nameRequest;
    private String processedNameRequest;
    private String title;
    private String description;
    private String phraseOfElement;
    private String textOfElement;
    private String commonQuestion;

    public Resource(String nameRequest, String title, String description, String phraseOfElement) {
        this.nameRequest = nameRequest;
        this.title = title;
        this.description = description;
        this.phraseOfElement = phraseOfElement;
    }

    public Resource(String nameRequest, String title, String description, String phraseOfElement, String commonQuestion) {
        this(nameRequest, title, description, phraseOfElement);
        this.commonQuestion = commonQuestion;
    }

    public String getCommonQuestion() {
        return commonQuestion;
    }

    public String getNameRequest() {
        return nameRequest;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPhraseOfElement() {
        return phraseOfElement;
    }

    public String getTextOfElement() {
        return textOfElement;
    }

    public void setTextOfElement(String textOfElement) {
        this.textOfElement = textOfElement;
    }

    public void setProcessedNameRequest(String processedNameRequest) {
        this.processedNameRequest = processedNameRequest;
    }
}
