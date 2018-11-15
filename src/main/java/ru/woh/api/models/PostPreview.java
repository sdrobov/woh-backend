package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import ru.woh.api.views.PostPreviewView;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "PostPreviewView")
@Table(name = "source_post_preview")
@SQLDelete(sql = "UPDATE source_post_preview SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findPostPreviewById")
@NamedQuery(name = "findPostPreviewById", query = "SELECT p FROM PostPreviewView p WHERE p.id = ?1 AND p.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class PostPreview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String title;

    @Type(type = "text")
    private String text;

    @Type(type = "text")
    private String announce;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Source source;

    public PostPreviewView view() {
        return new PostPreviewView(this.id, this.title, this.text, this.announce, this.createdAt, this.source);
    }
}
