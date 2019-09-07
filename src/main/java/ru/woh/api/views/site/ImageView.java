package ru.woh.api.views.site;

public class ImageView {
    private String url;
    private String content;
    private Integer width;
    private Integer height;

    public ImageView() {
    }

    public ImageView(String content, Integer width, Integer height) {
        this.content = content;
        this.width = width;
        this.height = height;
    }

    public ImageView(String url, String content, Integer width, Integer height) {
        this.url = url;
        this.content = content;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
