package ru.woh.api.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "CommentLikes")
@Table(name = "comment_likes")
public class CommentLikes implements Serializable {
    public CommentLikes() {
    }

    public CommentLikesPK getPk() {
        return this.pk;
    }

    public Comment getComment() {
        return this.comment;
    }

    public User getUser() {
        return this.user;
    }

    public Boolean getIsLike() {
        return this.isLike;
    }

    public void setPk(CommentLikesPK pk) {
        this.pk = pk;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    @Embeddable
    public static class CommentLikesPK implements Serializable {
        @Column(name = "comment_id", nullable = false)
        private Long commentId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        public CommentLikesPK(Long commentId, Long userId) {
            this.commentId = commentId;
            this.userId = userId;
        }

        public CommentLikesPK() {
        }

        public Long getCommentId() {
            return this.commentId;
        }

        public Long getUserId() {
            return this.userId;
        }

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CommentLikesPK)) {
                return false;
            }
            CommentLikesPK that = (CommentLikesPK) o;
            return commentId.equals(that.commentId) &&
                userId.equals(that.userId);
        }

        @Override public int hashCode() {
            return Objects.hash(commentId, userId);
        }
    }

    @EmbeddedId
    @Column(unique = true)
    private CommentLikesPK pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "`like`")
    private Boolean isLike;

    public CommentLikes(CommentLikesPK pk, Boolean isLike) {
        this.pk = pk;
        this.isLike = isLike;
    }
}
