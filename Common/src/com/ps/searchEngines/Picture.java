package com.ps.searchEngines;

public class Picture {
    private final String name;
    private final String url;
    private final int height;
    private final int width;
    private final String protocol;
    private String sizePic;

    public Picture(String name, String url, int height, int width, String protocol) {
        this.name = name;
        this.url = url;
        this.height = height;
        this.width = width;
        this.sizePic = height + "x" + width;
        this.protocol = protocol;
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

    public String getProtocol() {
        return protocol;
    }
}
