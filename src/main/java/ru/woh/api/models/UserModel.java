package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Getter
@Setter
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
}
