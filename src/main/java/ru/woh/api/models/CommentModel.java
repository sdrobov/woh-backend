package ru.woh.api.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.views.CommentView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Comments")
@Getter @Setter @NoArgsConstructor
public class CommentModel implements Serializable {
    private @Id @Setter(AccessLevel.PROTECTED) Long id;
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
}
