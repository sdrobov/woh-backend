package ru.woh.api.models;

import lombok.NoArgsConstructor;
import ru.woh.api.views.CommentView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Comments")
@NoArgsConstructor
public class CommentModel implements Serializable {
    private @Id Long id;
    private String text;
    private @Column(name = "created_at") Date createdAt;
    private @Column(name = "updated_at") Date updatedAt;
    private @Column(name = "deleted_at") Date deletedAt;
    private @ManyToOne @JoinColumn(name = "user_id") UserModel user;
    private @ManyToOne @JoinColumn(name = "post_id") PostModel post;

    public static CommentModel fromView(CommentView view) {
        CommentModel newComment = new CommentModel();
        newComment.text = view.getText();
        newComment.createdAt = new Date();

        return newComment;
    }

    public CommentView view() {
        return new CommentView(this.id, this.text, this.createdAt);
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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }
}
