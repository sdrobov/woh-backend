package ru.woh.api.models;

import lombok.NoArgsConstructor;
import ru.woh.api.views.UserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Users")
@NoArgsConstructor
public class UserModel implements Serializable {
    private @Id Long id;
    private String email;
    private String password;
    private @Column(name = "created_at") Date createdAt;
    private @Column(name = "updated_at") Date updatedAt;
    private String name;
    private String avatar;
    private String fb;
    private String vk;
    private String google;
    private @ManyToOne @JoinColumn(name = "role_id") RoleModel role;
    private @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL) Set<PostModel> moderatedPosts;
    private @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) Set<CommentModel> comments;

    public UserView view() {
        return new UserView(this.id, this.email, this.name, this.avatar);
    }

    public Boolean isAdmin() {
        return this.role != null && this.role.getName() == "admin";
    }

    public Boolean isModer() {
        return (this.role != null && this.role.getName() == "moder") || this.isAdmin();
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
}
