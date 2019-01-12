package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Comment;
import ru.woh.api.models.Post;

import java.util.Date;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    Page<Comment> findAllByPost(Post post, Pageable pageable);
    Long countByPost(Post post);

    default void delete(Comment comment) {
        comment.setDeletedAt(new Date());

        this.save(comment);
    }
}
