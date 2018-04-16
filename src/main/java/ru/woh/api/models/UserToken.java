package ru.woh.api.models;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@NoArgsConstructor
public class UserToken {
    @Id
    public String id;
    public String token;
    public Long userId;
    public Date expiresAt;
}
