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
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Post")
@Table(name = "Posts")
@SQLDelete(sql = "UPDATE Posts SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findPostById")
@NamedQuery(name = "findPostById", query = "SELECT p FROM Post p WHERE p.id = ?1 AND p.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class PostModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String text;
    private String source;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at") private Date deletedAt;
    @Column(name = "is_allowed") private Boolean isAllowed;
    @Column(name = "moderated_at") private Date moderatedAt;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    @CreatedBy
    private UserModel moderator;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) private Set<CommentModel> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TagsRefUsers",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<TagModel> tags = new HashSet<>();

    private Long rating;

    public PostView view() {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, this.comments, this.tags);
    }

    public PostView view(Boolean withComments) {
        return new PostView(
            this.id,
            this.title,
            this.text,
            this.source,
            this.createdAt,
            withComments ? this.comments : Collections.<CommentModel>emptySet(),
            this.tags
        );
    }

    public AdminPostView adminView() {
        return new AdminPostView(
            this.id,
            this.title,
            this.text,
            this.source,
            this.createdAt,
            Collections.<CommentModel>emptySet(),
            this.tags,
            this.updatedAt,
            this.moderatedAt,
            this.moderator,
            this.isAllowed
        );
    }

    public void approve(UserModel moderator) {
        this.isAllowed = true;
        this.moderatedAt = new Date();
        this.moderator = moderator;
    }

    public void dismiss(UserModel moderator) {
        this.isAllowed = false;
        this.moderatedAt = new Date();
        this.moderator = moderator;
    }
}
