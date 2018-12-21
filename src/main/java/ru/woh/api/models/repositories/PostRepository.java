package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Post;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt <= CURRENT_TIMESTAMP " +
        "ORDER BY p.publishedAt DESC")
    Page<Post> findAllAllowed(Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "LEFT JOIN Teaser t ON t.post = p AND t.from >= ?1 AND t.to <= ?2 " +
        "WHERE t.post IS NULL " +
        "AND p.isAllowed = 1 " +
        "AND p.publishedAt <= CURRENT_TIMESTAMP " +
        "ORDER BY p.publishedAt DESC")
    Page<Post> findAllNotTeasers(Date from, Date to, Pageable pageable);

    default Page<Post> findAllExceptTodayTeasers(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        Date from = Date.from(now.with(LocalDateTime.MIN).toInstant(ZoneOffset.UTC));
        Date to = Date.from(now.with(LocalDateTime.MAX).toInstant(ZoneOffset.UTC));

        return this.findAllNotTeasers(from, to, pageable);
    }

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt <= CURRENT_TIMESTAMP " +
        "AND p.tags IN ?1 " +
        "ORDER BY p.publishedAt DESC")
    Page<Post> findAllByTags(Set<String> tags, Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt <= ?1 " +
        "AND p.id <> ?2 " +
        "AND p.canBeNearest = 1 " +
        "ORDER BY p.publishedAt DESC")
    List<Post> findAllPrev(Date publishedAt, Long id, Pageable pageable);

    default List<Post> findPrev(Date publishedAt, Long id) {
        return this.findAllPrev(publishedAt, id, PageRequest.of(0, 3));
    }

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt >= ?1 " +
        "AND p.id <> ?2 " +
        "AND p.canBeNearest = 1 " +
        "ORDER BY p.publishedAt ASC")
    List<Post> findAllNext(Date publishedAt, Long id, Pageable pageable);

    default List<Post> findNext(Date publishedAt, Long id) {
        return this.findAllNext(publishedAt, id, PageRequest.of(0, 3));
    }

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt >= CURRENT_TIMESTAMP " +
        "ORDER BY p.publishedAt ASC")
    Page<Post> findWaitingForPublishing(Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 1 " +
        "AND p.publishedAt BETWEEN ?1 AND ?2 " +
        "ORDER BY p.publishedAt ASC")
    Page<Post> findWaitingForPublishingAt(Date publishedAtFrom, Date publishedAtTo, Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = 0 " +
        "AND p.moderatedAt IS NULL " +
        "ORDER BY p.createdAt ASC")
    Page<Post> findWaitingForModeration(Pageable pageable);
}
