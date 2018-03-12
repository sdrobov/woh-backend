package ru.woh.api.models;

import lombok.NoArgsConstructor;
import ru.woh.api.views.PostView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Posts")
@NoArgsConstructor
public class PostModel implements Serializable {
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String title;
    private String text;
    private String source;
    private @Column(name = "created_at") Date createdAt;
    private @Column(name = "updated_at") Date updatedAt;
    private @Column(name = "is_moderated") Boolean isModerated;
    private @Column(name = "is_allowed") Boolean isAllowed;
    private @Column(name = "moderated_at") Date moderatedAt;
    private @ManyToOne @JoinColumn(name = "moderator_id") UserModel moderator;
    private @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) Set<CommentModel> comments;

    public PostView view() {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, comments);
    }

    public PostView view(Boolean withComments) {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, withComments ? comments : Collections.<CommentModel>emptySet());
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getModerated() {
        return isModerated;
    }

    public void setModerated(Boolean moderated) {
        isModerated = moderated;
    }

    public Boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(Boolean allowed) {
        isAllowed = allowed;
    }

    public Date getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(Date moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public UserModel getModerator() {
        return moderator;
    }

    public void setModerator(UserModel moderator) {
        this.moderator = moderator;
    }

    public Set<CommentModel> getComments() {
        return comments;
    }

    public void setComments(Set<CommentModel> comments) {
        this.comments = comments;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
