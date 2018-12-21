package ru.woh.api.views.admin;

import ru.woh.api.models.Post;

import java.util.Date;

public class TeaserView {
    private Date from;
    private Date to;
    private AdminPostView post;
    private Boolean isTeaser;

    public TeaserView(Date from, Date to, Post post, Boolean isTeaser) {
        this.from = from;
        this.to = to;
        this.post = post.adminView();
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

    public AdminPostView getPost() {
        return post;
    }

    public void setPost(AdminPostView post) {
        this.post = post;
    }

    public Boolean getTeaser() {
        return isTeaser;
    }

    public void setTeaser(Boolean teaser) {
        isTeaser = teaser;
    }
}
