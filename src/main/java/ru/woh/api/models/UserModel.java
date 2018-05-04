package ru.woh.api.models;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.woh.api.views.UserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "User")
@Table(name = "Users")
@SQLDelete(sql = "UPDATE Users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findUserById")
@NamedQuery(name = "findUserById", query = "SELECT u FROM User u WHERE u.id = ?1 AND u.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true) private String email;
    private String password;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at") private Date deletedAt;
    private String name;
    private String avatar;
    @Column(unique = true) private String fb;
    @Column(unique = true) private String vk;
    @Column(unique = true) private String google;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel role;

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL) private Set<PostModel> moderatedPosts = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) private Set<CommentModel> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "TagsRefUsers",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<TagModel> tags = new HashSet<>();

    public UserView view() {
        return new UserView(this.id, this.email, this.name, this.avatar);
    }

    public Boolean isAdmin() {
        if (this.role == null) {
            return false;
        }

        return Objects.equals(this.role.getName(), "admin");
    }

    public Boolean isModer() {
        if (this.role == null) {
            return false;
        }

        return Objects.equals(this.role.getName(), "moder") || this.isAdmin();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public Set<PostModel> getModeratedPosts() {
        return moderatedPosts;
    }

    public void setModeratedPosts(Set<PostModel> moderatedPosts) {
        this.moderatedPosts = moderatedPosts;
    }

    public Set<CommentModel> getComments() {
        return comments;
    }

    public void setComments(Set<CommentModel> comments) {
        this.comments = comments;
    }

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }
}
