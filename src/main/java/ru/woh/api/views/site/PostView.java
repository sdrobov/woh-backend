package ru.woh.api.views.site;

import ru.woh.api.models.Tag;
import ru.woh.api.models.User;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostView {
    protected Long id;
    protected String title;
    protected String text;
    protected String announce;
    protected String source;
    protected Date createdAt;
    protected Date publishedAt;
    protected List<CommentView> comments;
    protected List<String> tags;
    protected RatingView rating;
    protected Long totalComments;
    protected UserView proposedBy;

    public PostView(
        Long id,
        String title,
        String text,
        String source,
        Date createdAt,
        Date publishedAt,
        Set<Tag> tags,
        String announce,
        User proposedBy
    ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.tags = tags != null ? tags.stream().map(Tag::getName).sorted().collect(Collectors.toList()) : null;
        this.announce = announce;
        this.proposedBy = proposedBy != null ? proposedBy.view() : null;
    }

    public PostView() {
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

    public String getSource() {
        return this.source;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getPublishedAt() {
        return this.publishedAt;
    }

    public List<CommentView> getComments() {
        return this.comments;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public RatingView getRating() {
        return this.rating;
    }

    public Long getTotalComments() {
        return this.totalComments;
    }

    public UserView getProposedBy() {
        return this.proposedBy;
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

    public void setSource(String source) {
        this.source = source;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setComments(List<CommentView> comments) {
        this.comments = comments;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setRating(RatingView rating) {
        this.rating = rating;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

    public void setProposedBy(UserView proposedBy) {
        this.proposedBy = proposedBy;
    }
}
