package ru.woh.api.views.site;

import lombok.Getter;
import lombok.Setter;
import ru.woh.api.models.Post;

import java.util.Set;

@Getter
@Setter
public class UserExtView extends UserView {
    protected String token;

    public UserExtView(
        Long id,
        String email,
        String name,
        String avatar,
        String role,
        String annotation,
        Set<Post> proposedPosts,
        String token
    ) {
        super(id, email, name, avatar, role, annotation, proposedPosts);
        this.token = token;
    }
}
