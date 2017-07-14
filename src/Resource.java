public class Resource {
    private String nameRequest;
    private String title;
    private String descripion;

    public Resource(String nameRequest, String title, String description) {
        this.nameRequest = nameRequest;
        this.title = title;
        descripion = description;
    }

    public String getNameRequest() {
        return nameRequest;
    }

    public String getTitle() {
        return title;
    }

    public String getDescripion() {
        return descripion;
    }
}
