package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "tag")
@NoArgsConstructor
@Getter
@Setter
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tags_ref_users",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tags_ref_posts",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Post> posts = new HashSet<>();
}
