package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import ru.woh.api.views.admin.SourceView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "Source")
@Table(name = "source")
@Getter
@Setter
@NoArgsConstructor
public class Source implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String url;

    @Type(type = "text")
    private String settings;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "last_post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPostDate;

    @Column(name = "is_locked")
    private Integer isLocked;

    @OneToMany(mappedBy = "sourceSite", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<Post> posts;

    public SourceView view() {
        return new SourceView(
            this.id,
            this.name,
            this.url,
            this.settings,
            this.createdAt,
            this.lastPostDate,
            this.isLocked == 1,
            this.posts
        );
    }
}
