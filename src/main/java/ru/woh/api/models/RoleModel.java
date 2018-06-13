package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@Getter
@Setter
public class RoleModel implements Serializable {
    public static final String ANONYMOUS = "ANONYMOUS";
    public static final String USER = "USER";
    public static final String MODER = "MODER";
    public static final String ADMIN = "ADMIN";
    public static final String PREFIX = "ROLE_%s";
    public static final String ROLE_ANONYMOUS = String.format("%s%s", PREFIX, ANONYMOUS);
    public static final String ROLE_USER = String.format("%s%s", PREFIX, USER);
    public static final String ROLE_MODER = String.format("%s%s", PREFIX, MODER);
    public static final String ROLE_ADMIN = String.format("%s%s", PREFIX, ADMIN);

    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String name;

    private @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) Set<UserModel> users;
}
