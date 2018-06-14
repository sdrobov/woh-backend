package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Comment;
import ru.woh.api.models.User;

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

    public CommentView(Long id, String text, Date createdAt, Date updatedAt, User user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user.view();
    }

    public Comment model() {
        Comment newComment = new Comment();
        newComment.setText(this.getText());
        newComment.setCreatedAt(new Date());

        return newComment;
    }
}
