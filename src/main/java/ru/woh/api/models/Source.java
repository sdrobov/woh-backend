package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "Source")
@Table(name = "source")
@Getter
@Setter
@NoArgsConstructor
class Source implements Serializable {
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
    @CreatedDate
    private Date createdAt;

    @Column(name = "last_post_date")
    private Date lastPostDate;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @OneToMany(mappedBy = "sourceSite", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Set<Post> posts;
}