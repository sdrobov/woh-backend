package ru.woh.api.views.admin;

import java.util.Date;

public class TeaserView {
    private Date from;
    private Date to;
    private long post;
    private boolean isTeaser;

    public TeaserView() {}

    public TeaserView(Date from, Date to, long post, boolean isTeaser) {
        this.from = from;
        this.to = to;
        this.post = post;
        this.isTeaser = isTeaser;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public long getPost() {
        return post;
    }

    public void setPost(long post) {
        this.post = post;
    }

    public boolean getIsTeaser() {
        return isTeaser;
    }

    public void setIsTeaser(boolean teaser) {
        isTeaser = teaser;
    }
}
