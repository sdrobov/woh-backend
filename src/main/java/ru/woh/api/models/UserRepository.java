package ru.woh.api.models;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserModel, Long> {
    UserModel findFirstByEmail(String email);
}
