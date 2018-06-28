package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {
    public static final String ANONYMOUS = "ANONYMOUS";
    public static final String USER = "USER";
    public static final String MODER = "MODER";
    public static final String ADMIN = "ADMIN";
    public static final String PREFIX = "ROLE_%s";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_MODER = "ROLE_MODER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users;
}
