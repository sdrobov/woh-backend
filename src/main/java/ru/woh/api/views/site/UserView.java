package ru.woh.api.views.site;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Post;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class UserView {
    protected Long id;
    protected String email;
    protected String name;
    protected String avatar;
    protected String role;
    protected String annotation;
    protected List<PostView> proposedPosts;

    public UserView(
        Long id,
        String email,
        String name,
        String avatar,
        String role,
        String annotation,
        Set<Post> proposedPosts
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.annotation = annotation;
        this.proposedPosts = proposedPosts != null
            ? proposedPosts.stream().map(Post::view).collect(Collectors.toList())
            : null;
    }
}
