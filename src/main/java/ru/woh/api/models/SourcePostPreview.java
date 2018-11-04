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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "SourcePostPreview")
@Table(name = "source_post_preview")
@SQLDelete(sql = "UPDATE source_post_preview SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findSourcePostPreviewById")
@NamedQuery(name = "findSourcePostPreviewById", query = "SELECT p FROM SourcePostPreview p WHERE p.id = ?1 AND p.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
public class SourcePostPreview implements Serializable {
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

    @ManyToOne
    @JoinColumn(name = "source_id")
    @CreatedBy
    private Source source;
}
