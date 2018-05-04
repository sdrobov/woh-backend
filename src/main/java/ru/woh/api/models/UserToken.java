package ru.woh.api.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class UserToken {
    @Id
    public String id;
    public String token;
    public Long userId;
    public Date expiresAt;
}
