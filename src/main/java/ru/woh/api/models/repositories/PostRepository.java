package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Post;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt <= CURRENT_TIMESTAMP ORDER BY publishedAt DESC")
    Page<Post> findAllAllowed(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt <= CURRENT_TIMESTAMP AND tags IN ?1 ORDER BY publishedAt DESC")
    Page<Post> findAllByTags(Set<String> tags, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt <= ?1 AND id <> ?2 ORDER BY publishedAt DESC")
    List<Post> findAllPrev(Date publishedAt, Long id, Pageable pageable);

    default List<Post> findPrev(Date publishedAt, Long id) {
        return this.findAllPrev(publishedAt, id, PageRequest.of(0, 3));
    }

    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt >= ?1 AND id <> ?2 ORDER BY publishedAt ASC")
    List<Post> findAllNext(Date publishedAt, Long id, Pageable pageable);

    default List<Post> findNext(Date publishedAt, Long id) {
        return this.findAllNext(publishedAt, id, PageRequest.of(0, 3));
    }

    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt >= CURRENT_TIMESTAMP ORDER BY publishedAt ASC")
    Page<Post> findWaitingForPublishing(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE isAllowed = 1 AND publishedAt BETWEEN ?1 AND ?2 ORDER BY publishedAt ASC")
    Page<Post> findWaitingForPublishingAt(Date publishedAtFrom, Date publishedAtTo, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE isAllowed = 0 AND moderatedAt IS NULL ORDER BY createdAt ASC")
    Page<Post> findWaitingForModeration(Pageable pageable);
}
