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
    private @Id @GeneratedValue(strategy = GenerationType.AUTO) Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserModel> users) {
        this.users = users;
    }

    private @OneToMany(mappedBy = "role", cascade = CascadeType.ALL) Set<UserModel> users;
}
