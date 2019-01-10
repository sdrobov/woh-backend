package ru.woh.api.views.site;

import ru.woh.api.models.Category;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PostView {
    protected Long id;
    private String title;
    protected String text;
    private String announce;
    protected String source;
    protected Date createdAt;
    private Date publishedAt;
    private List<CommentView> comments;
    private List<String> tags;
    private List<String> categories;
    private RatingView rating;
    private Long totalComments;
    private UserView proposedBy;
    private String teaserImage;
    private String featuredImage;
    private String nearestImage;
    private Boolean canBeNearest;

    public PostView() {
    }

    public PostView(
        Long id,
        String title,
        String text,
        String source,
        Date createdAt,
        Date publishedAt,
        Set<Tag> tags,
        Set<Category> categories, String announce,
        User proposedBy,
        String teaserImage,
        String featuredImage, String nearestImage, Boolean canBeNearest
    ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.categories = categories != null ? categories.stream()
            .map(Category::getName)
            .sorted()
            .collect(Collectors.toList()) : null;
        this.announce = announce;
        this.source = source;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.tags = tags != null ? tags.stream()
            .map(Tag::getName)
            .sorted()
            .collect(Collectors.toList()) : null;
        this.proposedBy = proposedBy != null ? proposedBy.view() : null;
        this.teaserImage = teaserImage;
        this.featuredImage = featuredImage;
        this.nearestImage = nearestImage;
        this.canBeNearest = canBeNearest;
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

    public String getTeaserImage() {
        return teaserImage;
    }

    public void setTeaserImage(String teaserImage) {
        this.teaserImage = teaserImage;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getNearestImage() {
        return nearestImage;
    }

    public void setNearestImage(String nearestImage) {
        this.nearestImage = nearestImage;
    }

    public Boolean getCanBeNearest() {
        return canBeNearest;
    }

    public void setCanBeNearest(Boolean canBeNearest) {
        this.canBeNearest = canBeNearest;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
