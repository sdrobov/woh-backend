package ru.woh.api.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "PostLikes")
@Table(name = "post_likes")
public class PostLikes implements Serializable {
    public PostLikesPK getPk() {
        return this.pk;
    }

    public Post getPost() {
        return this.post;
    }

    public User getUser() {
        return this.user;
    }

    public Boolean getIsLike() {
        return this.isLike;
    }

    public void setPk(PostLikesPK pk) {
        this.pk = pk;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    @Embeddable
    public static class PostLikesPK implements Serializable {
        @Column(name = "post_id", nullable = false)
        private Long postId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        public PostLikesPK() {

        }

        public PostLikesPK(Long postId, Long userId) {
            this.postId = postId;
            this.userId = userId;
        }

        public Long getPostId() {
            return this.postId;
        }

        public Long getUserId() {
            return this.userId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PostLikesPK)) {
                return false;
            }
            PostLikesPK that = (PostLikesPK) o;
            return postId.equals(that.postId) &&
                userId.equals(that.userId);
        }

        @Override public int hashCode() {
            return Objects.hash(postId, userId);
        }
    }

    @EmbeddedId
    @Column(unique = true)
    private PostLikesPK pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "`like`")
    private Boolean isLike;

    public PostLikes() {

    }

    public PostLikes(PostLikesPK pk, Boolean isLike) {
        this.pk = pk;
        this.isLike = isLike;
    }
}
