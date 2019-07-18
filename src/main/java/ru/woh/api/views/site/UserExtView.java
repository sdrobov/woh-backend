package ru.woh.api.views.site;

import java.util.Date;

public class UserExtView extends UserView {
    private String token;

    public UserExtView() {
    }

    public UserExtView(
        Long id,
        String email,
        String name,
        String avatar,
        String role,
        String annotation,
        Date createdAt,
        String token
    ) {
        super(id, email, name, avatar, role, annotation, createdAt);
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
