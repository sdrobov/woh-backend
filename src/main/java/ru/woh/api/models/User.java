package ru.woh.api.models;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import ru.woh.api.views.site.UserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user")
@Where(clause = "deleted_at IS NULL")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    private String name;
    private String avatar;

    @Column(unique = true)
    private String fb;

    @Column(unique = true)
    private String vk;

    @Column(unique = true)
    private String google;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "moderator", fetch = FetchType.LAZY)
    private Set<Post> moderatedPosts = new HashSet<>();

    @OneToMany(mappedBy = "proposedBy", fetch = FetchType.LAZY)
    private Set<Post> proposedPosts = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tags_ref_users",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private Set<Tag> tags = new HashSet<>();

    public User() {
    }

    public static AnonymousAuthenticationToken anonymousAuthenticationToken() {
        return new AnonymousAuthenticationToken(
            "key",
            "anonymousUser",
            AuthorityUtils.createAuthorityList(Role.ROLE_ANONYMOUS)
        );
    }

    public UserView view() {
        return new UserView(
            this.id,
            this.email,
            this.name,
            this.avatar,
            this.getRoleName(),
            this.annotation,
            this.createdAt);
    }

    private Boolean isAdmin() {
        return Objects.equals(this.getRoleName(), Role.ROLE_ADMIN);
    }

    public Boolean isModer() {
        return Objects.equals(this.getRoleName(), Role.ROLE_MODER) || this.isAdmin();
    }

    public UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(
            this,
            this.getToken(),
            AuthorityUtils.createAuthorityList(this.getRoleName())
        );
    }

    public String getRoleName() {
        return this.getRole() != null ? String.format(
            Role.PREFIX,
            this.getRole().getName().toUpperCase()
        ) : Role.ROLE_USER;
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getToken() {
        return this.token;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public Date getDeletedAt() {
        return this.deletedAt;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getFb() {
        return this.fb;
    }

    public String getVk() {
        return this.vk;
    }

    public String getGoogle() {
        return this.google;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public Role getRole() {
        return this.role;
    }

    public Set<Post> getModeratedPosts() {
        return this.moderatedPosts;
    }

    public Set<Post> getProposedPosts() {
        return this.proposedPosts;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setModeratedPosts(Set<Post> moderatedPosts) {
        this.moderatedPosts = moderatedPosts;
    }

    public void setProposedPosts(Set<Post> proposedPosts) {
        this.proposedPosts = proposedPosts;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
