package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "PostLikes")
@Table(name = "post_likes")
@Getter
@Setter
@NoArgsConstructor
public class PostLikes implements Serializable {
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostLikesPK implements Serializable {
        @Column(name = "post_id", nullable = false)
        private Long postId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        public PostLikesPK(Long postId, Long userId) {
            this.postId = postId;
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
