package ru.woh.api.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "PostLikes")
@Table(name = "post_likes")
public class PostLikes implements Serializable {
    public PostLikes() {
    }

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

        public PostLikesPK(Long postId, Long userId) {
            this.postId = postId;
            this.userId = userId;
        }

        public PostLikesPK() {
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

    public PostLikes(PostLikesPK pk, Boolean isLike) {
        this.pk = pk;
        this.isLike = isLike;
    }
}
