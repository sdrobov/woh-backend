package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import ru.woh.api.views.UserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "User")
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Loader(namedQuery = "findUserById")
@NamedQuery(name = "findUserById", query = "SELECT u FROM User u WHERE u.id = ?1 AND u.deletedAt IS NULL")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor
@Getter
@Setter
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
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @Column(name = "deleted_at")
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

    @OneToMany(mappedBy = "moderator",
        cascade = CascadeType.ALL)
    private Set<Post> moderatedPosts = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tags_ref_users",
        joinColumns = {@JoinColumn(name = "user_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private Set<Tag> tags = new HashSet<>();

    public static AnonymousAuthenticationToken anonymousAuthenticationToken() {
        return new AnonymousAuthenticationToken(
            "key",
            "anonymousUser",
            AuthorityUtils.createAuthorityList(Role.ROLE_ANONYMOUS)
        );
    }

    public UserView view() {
        return new UserView(this.id, this.email, this.name, this.avatar, this.getRoleName(), this.annotation);
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
}
