package ru.woh.api.views.site;

import lombok.Getter;
import lombok.Setter;

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
        String token
    ) {
        super(id, email, name, avatar, role, annotation);
        this.token = token;
    }
}
