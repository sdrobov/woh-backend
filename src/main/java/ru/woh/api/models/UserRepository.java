package ru.woh.api.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, Long> {
    Optional<UserModel> findFirstByEmail(String email);

    Optional<UserModel> findFirstByToken(String token);
}
