package ru.woh.api.views.site;

import ru.woh.api.models.Comment;
import ru.woh.api.models.User;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

public class CommentView {
    private Long id;
    private String text;
    private Date createdAt;
    private Date updatedAt;
    private UserView user;
    private ReplyTo replyTo;
    private RatingView rating;
    private List<MediaView> media;

    public CommentView() {
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

    public UserView getUser() {
        return this.user;
    }

    public ReplyTo getReplyTo() {
        return this.replyTo;
    }

    public RatingView getRating() {
        return this.rating;
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

    public void setUser(UserView user) {
        this.user = user;
    }

    public void setReplyTo(ReplyTo replyTo) {
        this.replyTo = replyTo;
    }

    public void setRating(RatingView rating) {
        this.rating = rating;
    }

    public List<MediaView> getMedia() {
        return media;
    }

    public void setMedia(List<MediaView> media) {
        this.media = media;
    }

    public static class ReplyTo {
        private Long id;
        private String text;
        private UserView user;
        private Date createdAt;

        ReplyTo(Long id, String text, UserView user, Date createdAt) {
            this.id = id;
            this.text = text;
            this.user = user;
            this.createdAt = createdAt;
        }

        public ReplyTo() {
        }

        public Long getId() {
            return this.id;
        }

        public String getText() {
            return this.text;
        }

        public UserView getUser() {
            return this.user;
        }

        public Date getCreatedAt() {
            return this.createdAt;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setUser(UserView user) {
            this.user = user;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }

    public CommentView(Long id, String text, Date createdAt, Date updatedAt, User user, Comment replyTo) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user.view();
        try {
            if (replyTo != null) {
                this.replyTo = new ReplyTo(replyTo.getId(),
                    replyTo.getText(),
                    replyTo.getUser().view(),
                    replyTo.getCreatedAt());
            }
        } catch (EntityNotFoundException e) {
            this.replyTo = new ReplyTo(null, "[deleted]", null, null);
        }
    }

    public Comment model() {
        Comment newComment = new Comment();
        newComment.setText(this.getText());
        newComment.setCreatedAt(new Date());

        return newComment;
    }
}
