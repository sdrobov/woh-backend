package ru.woh.api.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.views.PostView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Posts")
@Getter @Setter @NoArgsConstructor
public class PostModel implements Serializable {
    private @Id @Setter(AccessLevel.PROTECTED) Long id;
    private String title;
    private String text;
    private String source;
    private @Column(name = "created_at") Date createdAt;
    private @Column(name = "updated_at") Date updatedAt;
    private @Column(name = "is_moderated") Boolean isModerated;
    private @Column(name = "is_allowed") Boolean isAllowed;
    private @Column(name = "moderated_at") Date moderatedAt;
    private @ManyToOne @JoinColumn(name = "moderator_id") UserModel moderator;
    private @OneToMany(mappedBy = "post", cascade = CascadeType.ALL) Set<CommentModel> comments;

    public PostView view() {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, comments);
    }

    public PostView view(Boolean withComments) {
        return new PostView(this.id, this.title, this.text, this.source, this.createdAt, withComments ? comments : Collections.<CommentModel>emptySet());
    }
}
