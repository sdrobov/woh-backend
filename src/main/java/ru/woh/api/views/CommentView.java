package ru.woh.api.views;

import lombok.NoArgsConstructor;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.UserModel;

import java.util.Date;

@NoArgsConstructor
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    public PostView getPost() {
        return post;
    }

    public void setPost(PostView post) {
        this.post = post;
    }
}
