package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.CommentModel;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.UserModel;

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
    private PostView post;

    public CommentView(Long id, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }

    public CommentView(Long id, String text, Date createdAt, UserModel user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user.view();
    }

    public CommentView(Long id, String text, Date createdAt, PostModel post) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.post = post.view(false);
    }

    public CommentView(Long id, String text, Date createdAt, UserModel user, PostModel post) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user.view();
        this.post = post.view(false);
    }

    public CommentModel model() {
        CommentModel newComment = new CommentModel();
        newComment.setText(this.getText());
        newComment.setCreatedAt(new Date());

        return newComment;
    }
}
