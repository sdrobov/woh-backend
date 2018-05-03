package ru.woh.api.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends UndeletableRepository<PostModel, Long> {
    @Query("SELECT p FROM PostModel p WHERE is_allowed = TRUE ORDER BY created_at DESC")
    Page<PostModel> findAllApproved(Pageable pageable);
}
