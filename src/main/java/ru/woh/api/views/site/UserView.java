package ru.woh.api.views.site;

public class UserView {
    protected Long id;
    private String email;
    protected String name;
    private String avatar;
    protected String role;
    protected String annotation;

    public UserView(
        Long id,
        String email,
        String name,
        String avatar,
        String role,
        String annotation
    ) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.annotation = annotation;
    }

    public UserView() {
    }

    public Long getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public String getRole() {
        return this.role;
    }

    public String getAnnotation() {
        return this.annotation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
}
