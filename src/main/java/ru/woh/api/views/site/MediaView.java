package ru.woh.api.views.site;

public class MediaView {
    private Long id;
    private String title;
    private String url;
    private String embedCode;
    private String thumbnail;

    public MediaView() {
    }

    public MediaView(Long id, String title, String url, String embedCode, String thumbnail) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.embedCode = embedCode;
        if (thumbnail != null) {
            this.thumbnail = String.format("/image/%s", thumbnail);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
