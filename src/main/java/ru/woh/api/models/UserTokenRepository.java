package ru.woh.api.models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserTokenRepository extends MongoRepository<UserToken, String> {
    public UserToken findByToken(String token);
    public List<UserToken> findAllByUserId(Long userId);
}
