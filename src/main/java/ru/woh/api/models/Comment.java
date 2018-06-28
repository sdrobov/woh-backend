package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.woh.api.views.CommentView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Comment")
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findCommentById")
@NamedQuery(name = "findCommentById", query = "SELECT c FROM Comment c WHERE c.id = ?1 AND c.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String text;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at") private Date deletedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @CreatedBy
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Long rating;

    public CommentView view() {
        return new CommentView(this.id, this.text, this.createdAt, this.updatedAt, this.user);
    }
}
