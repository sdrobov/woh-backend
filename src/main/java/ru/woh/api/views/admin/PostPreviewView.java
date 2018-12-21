package ru.woh.api.views.admin;

import ru.woh.api.models.Source;

import java.util.Date;

public class PostPreviewView {
    private Long id;
    private String title;
    private String text;
    private String announce;
    private Date createdAt;
    private SourceView source;

    public PostPreviewView(
        Long id,
        String title,
        String text,
        String announce,
        Date createdAt,
        Source source
    ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.announce = announce;
        this.createdAt = createdAt;
        this.source = source.view();
    }

    public PostPreviewView() {
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getText() {
        return this.text;
    }

    public String getAnnounce() {
        return this.announce;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public SourceView getSource() {
        return this.source;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setSource(SourceView source) {
        this.source = source;
    }
}
