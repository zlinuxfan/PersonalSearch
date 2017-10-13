public class Picture {
    private final String name;
    private final String url;
    private final int height;
    private final int width;
    private String sizePic;

    public Picture(String name, String url, int height, int width) {
        this.name = name;
        this.url = url;
        this.height = height;
        this.width = width;
        this.sizePic = height + "x" + width;
    }

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public String getSizePic() {
        return sizePic;
    }
}
