package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserView {
    private Long id;
    private String email;
    private String name;
    private String avatar;

    public UserView(Long id, String email, String name, String avatar) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
    }
}
