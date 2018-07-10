package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Comment;
import ru.woh.api.models.User;

import javax.persistence.EntityNotFoundException;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CommentView {
    private Long id;
    private String text;
    private Date createdAt;
    private Date updatedAt;
    private UserView user;
    private ReplyTo replyTo;
    private RatingView rating;

    @Getter
    @Setter
    @NoArgsConstructor
    public class ReplyTo {
        private Long id;
        private String text;
        private UserView user;
        private Date createdAt;

        public ReplyTo(Long id) {
            this.id = id;
        }

        public ReplyTo(Long id, String text, UserView user, Date createdAt) {
            this.id = id;
            this.text = text;
            this.user = user;
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
                this.replyTo = new ReplyTo(replyTo.getId(), replyTo.getText(), replyTo.getUser().view(), replyTo.getCreatedAt());
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
