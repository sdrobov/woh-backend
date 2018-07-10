package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Post")
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findPostById")
@NamedQuery(name = "findPostById", query = "SELECT p FROM Post p WHERE p.id = ?1 AND p.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String title;

    @Type(type = "text")
    private String text;

    @Type(type = "text")
    private String announce;

    private String source;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "is_allowed")
    private Boolean isAllowed;

    @Column(name = "moderated_at")
    private Date moderatedAt;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    @CreatedBy
    private User moderator;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tags_ref_posts",
        joinColumns = {@JoinColumn(name = "post_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<PostLikes> likes;

    private Long rating;

    public PostView view() {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, this.tags, this.announce);
    }

    public AdminPostView adminView() {
        return new AdminPostView(
            this.id,
            this.title,
            this.text,
            this.source,
            this.createdAt,
            this.tags,
            this.announce,
            this.updatedAt,
            this.moderatedAt,
            this.moderator,
            this.isAllowed
        );
    }

    public void approve(User moderator) {
        this.isAllowed = true;
        this.moderatedAt = new Date();
        this.moderator = moderator;
    }

    public void dismiss(User moderator) {
        this.isAllowed = false;
        this.moderatedAt = new Date();
        this.moderator = moderator;
    }
}
