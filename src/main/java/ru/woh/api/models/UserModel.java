package ru.woh.api.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.views.UserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter @Setter @NoArgsConstructor
public class UserModel implements Serializable {
    private @Id @Setter(AccessLevel.PROTECTED) Long id;
    private String email;
    private String password;
    private @Column(name = "created_at") Date createdAt;
    private @Column(name = "updated_at") Date updatedAt;
    private String name;
    private String avatar;
    private String fb;
    private String vk;
    private String google;
    private @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL) Set<PostModel> moderatedPosts;
    private @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) Set<CommentModel> comments;

    public UserView view() {
        return new UserView(this.id, this.email, this.name, this.avatar);
    }
}
