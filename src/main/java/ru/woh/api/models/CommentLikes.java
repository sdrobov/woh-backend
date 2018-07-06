package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "CommentLikes")
@Table(name = "comment_likes")
@Getter
@Setter
@NoArgsConstructor
public class CommentLikes implements Serializable {
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CommentLikesPK implements Serializable {
        @Column(name = "comment_id", nullable = false)
        private Long commentId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        public CommentLikesPK(Long commentId, Long userId) {
            this.commentId = commentId;
            this.userId = userId;
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
