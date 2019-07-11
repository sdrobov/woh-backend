package ru.woh.api.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Tag")
@Table(name = "tag")
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "tags_ref_users",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "tags_ref_posts",
        joinColumns = {@JoinColumn(name = "tag_id")},
        inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Post> posts = new HashSet<>();

    public Tag() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
}
