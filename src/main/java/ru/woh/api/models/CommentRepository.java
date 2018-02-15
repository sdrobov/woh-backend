package ru.woh.api.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<CommentModel, Long> {
    Page<CommentModel> findAllByPost(PostModel post, Pageable pageable);
}
