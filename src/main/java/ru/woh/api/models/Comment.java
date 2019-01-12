package ru.woh.api.models;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.woh.api.views.site.CommentView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "Comment")
@Table(name = "comment")
@Where(clause = "deleted_at IS NULL")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Type(type = "text")
    private String text;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at") private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @CreatedBy
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Long rating;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_comment_id")
    private Comment replyTo;

    @OneToMany(mappedBy = "replyTo", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private Set<Comment> replies;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<CommentLikes> likes;

    public Comment() {
    }

    public CommentView view() {
        return new CommentView(this.id, this.text, this.createdAt, this.updatedAt, this.user, this.replyTo);
    }

    public Long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
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

    public User getUser() {
        return this.user;
    }

    public Post getPost() {
        return this.post;
    }

    public Long getRating() {
        return this.rating != null ? this.rating : 0;
    }

    public Comment getReplyTo() {
        return this.replyTo;
    }

    public Set<Comment> getReplies() {
        return this.replies;
    }

    public Set<CommentLikes> getLikes() {
        return this.likes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
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

    public void setUser(User user) {
        this.user = user;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public void modifyRating(Integer mod) {
        this.rating = this.getRating() + mod;
    }

    public void setReplyTo(Comment replyTo) {
        this.replyTo = replyTo;
    }

    public void setReplies(Set<Comment> replies) {
        this.replies = replies;
    }

    public void setLikes(Set<CommentLikes> likes) {
        this.likes = likes;
    }
}
