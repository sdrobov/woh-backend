package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserView {
    protected Long id;
    protected String email;
    protected String name;
    protected String avatar;
    protected String role;

    public UserView(Long id, String email, String name, String avatar, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
    }
}
