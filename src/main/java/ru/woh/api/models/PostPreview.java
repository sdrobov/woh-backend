package ru.woh.api.models;

import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import ru.woh.api.views.admin.PostPreviewView;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PostPreviewView")
@Table(name = "source_post_preview")
@SQLDelete(sql = "UPDATE source_post_preview SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findPostPreviewById")
@NamedQuery(name = "findPostPreviewById", query = "SELECT p FROM PostPreviewView p WHERE p.id = ?1 AND p.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
public class PostPreview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String title;

    @Type(type = "text")
    private String text;

    @Type(type = "text")
    private String announce;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    public PostPreview() {
    }

    public PostPreviewView view() {
        return new PostPreviewView(this.id, this.title, this.text, this.announce, this.createdAt, this.source);
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

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public Source getSource() {
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

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
