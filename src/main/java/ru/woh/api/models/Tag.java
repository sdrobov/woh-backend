package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.views.TagView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "Tags")
@NoArgsConstructor
@Getter
@Setter
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TagsRefUsers",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TagsRefPosts",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Post> posts = new HashSet<>();

    public TagView view() {
        return new TagView(this.id, this.name);
    }
}
