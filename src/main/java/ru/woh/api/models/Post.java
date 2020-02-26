package ru.woh.api.models;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.woh.api.views.admin.AdminPostView;
import ru.woh.api.views.site.PostView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Post")
@Table(name = "post")
@Where(clause = "deleted_at IS NULL")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String title;

    @Type(type = "text")
    private String text;

    @Type(type = "text")
    private String announce;

    private String source;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @Column(name = "published_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @Column(name = "is_allowed")
    private Short isAllowed = 0;

    @Column(name = "moderated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moderatedAt;

    @Column(name = "teaser_image")
    private String teaserImage;

    @Column(name = "featured_image")
    private String featuredImage;

    @Column(name = "nearest_image")
    private String nearestImage;

    @Column(name = "can_be_nearest")
    private Short canBeNearest = 1;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    @CreatedBy
    private User moderator;

    @ManyToOne
    @JoinColumn(name = "proposed_by")
    private User proposedBy;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tags_ref_posts",
        joinColumns = {@JoinColumn(name = "post_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "categories_ref_posts",
        joinColumns = {@JoinColumn(name = "post_id")},
        inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private Set<PostLikes> likes;

    private Long rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source sourceSite;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Teaser> teasers;

    @ManyToMany
    @JoinTable(name = "post_ref_media",
        joinColumns = {@JoinColumn(name = "post_id")},
        inverseJoinColumns = {@JoinColumn(name = "media_id")})
    private Set<Media> media = new HashSet<>();

    public Post() {
    }

    public PostView view() {
        return new PostView(
            this.id,
            this.title,
            this.text,
            this.sourceSite,
            this.createdAt,
            this.publishedAt,
            this.tags,
            this.categories, this.announce,
            this.proposedBy,
            this.teaserImage,
            this.featuredImage,
            this.nearestImage,
            this.canBeNearest == 1,
            this.teasers != null && !this.teasers.isEmpty() ? this.teasers.get(this.teasers.size() - 1) : null
        );
    }

    public AdminPostView adminView() {
        return new AdminPostView(
            this.id,
            this.title,
            this.text,
            this.sourceSite,
            this.createdAt,
            this.publishedAt,
            this.tags,
            this.categories,
            this.announce,
            this.proposedBy,
            this.teaserImage,
            this.featuredImage,
            this.nearestImage,
            this.canBeNearest == 1,
            this.teasers != null && !this.teasers.isEmpty() ? this.teasers.get(this.teasers.size() - 1) : null,
            this.updatedAt,
            this.moderatedAt,
            this.moderator,
            this.isAllowed == 1
        );
    }

    public void approve(User moderator) {
        this.isAllowed = 1;
        this.moderatedAt = new Date();
        this.publishedAt = new Date();
        this.moderator = moderator;
    }

    public void dismiss(User moderator) {
        this.isAllowed = 0;
        this.moderatedAt = new Date();
        this.moderator = moderator;
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

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public Date getPublishedAt() {
        return this.publishedAt;
    }

    public Short getIsAllowed() {
        return this.isAllowed;
    }

    public Date getModeratedAt() {
        return this.moderatedAt;
    }

    public User getModerator() {
        return this.moderator;
    }

    public User getProposedBy() {
        return this.proposedBy;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public Set<PostLikes> getLikes() {
        return this.likes;
    }

    public Long getRating() {
        return this.rating != null ? this.rating : 0;
    }

    public Source getSourceSite() {
        return this.sourceSite;
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

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public void setIsAllowed(Short isAllowed) {
        this.isAllowed = isAllowed;
    }

    public void setModeratedAt(Date moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public void setModerator(User moderator) {
        this.moderator = moderator;
    }

    public void setProposedBy(User proposedBy) {
        this.proposedBy = proposedBy;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setLikes(Set<PostLikes> likes) {
        this.likes = likes;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public void modifyRating(Integer mod) {
        this.rating = this.getRating() + mod;
    }

    public void setSourceSite(Source sourceSite) {
        this.sourceSite = sourceSite;
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

    public Short getCanBeNearest() {
        return canBeNearest;
    }

    public void setCanBeNearest(Short canBeNearest) {
        this.canBeNearest = canBeNearest;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<Teaser> getTeasers() {
        return teasers;
    }

    public void setTeasers(List<Teaser> teasers) {
        this.teasers = teasers;
    }

    public Set<Media> getMedia() {
        return media;
    }

    public void setMedia(Set<Media> media) {
        this.media = media;
    }
}
