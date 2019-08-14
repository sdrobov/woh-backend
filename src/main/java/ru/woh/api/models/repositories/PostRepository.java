package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Post;
import ru.woh.api.services.DateTimeService;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Page<Post> findAllByIsAllowedAndPublishedAtLessThanEqual(Short isAllowed, Date publishedAt, Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "LEFT JOIN Teaser t ON t.post = p AND CURRENT_TIMESTAMP BETWEEN t.from AND t.to " +
        "WHERE t.post IS NULL " +
        "AND p.isAllowed = 1 " +
        "AND p.publishedAt <= CURRENT_TIMESTAMP " +
        "ORDER BY p.publishedAt DESC")
    Page<Post> findAllExceptTodayTeasers(Pageable pageable);

    Page<Post> findAllByTags_Name(Set<String> tags, Pageable pageable);

    Page<Post> findAllByCategories_NameIn(Set<String> categories, Pageable pageable);

    Page<Post> findAllByIdIn(List<Long> ids, Pageable pageable);

    List<Post> findAllByIsAllowedAndPublishedAtLessThanEqualAndIdNotAndCanBeNearest(
        Short isAllowed,
        Date publishedAt,
        Long id,
        Short canBeNearest,
        Pageable pageable
    );

    List<Post> findAllByIsAllowedAndPublishedAtGreaterThanEqualAndIdNotAndCanBeNearest(
        Short isAllowed,
        Date publishedAt,
        Long id,
        Short canBeNearest,
        Pageable pageable
    );

    Page<Post> findAllByIsAllowedAndPublishedAtGreaterThanEqual(Short isAllowed, Date publishedAt, Pageable pageable);

    @Query("SELECT p FROM Post p " +
        "WHERE p.isAllowed = ?1 " +
        "AND (p.publishedAt between ?2 and ?3 OR p.publishedAt IS NULL)")
    Page<Post> findAllByIsAllowedAndPublishedAtBetween(Short isAllowed, Date from, Date to, Pageable pageable);

    Page<Post> findAllByIsAllowedAndModeratedAtIsNull(Short isAllowed, Pageable pageable);

    default Page<Post> findAllAllowed(Pageable pageable) {
        return this.findAllByIsAllowedAndPublishedAtLessThanEqual((short) 1, new Date(), pageable);
    }

    default List<Post> findPrev(Date publishedAt, Long id) {
        return this.findAllByIsAllowedAndPublishedAtLessThanEqualAndIdNotAndCanBeNearest(
            (short) 1,
            publishedAt,
            id,
            (short) 1,
            PageRequest.of(0, 3)
        );
    }

    default List<Post> findNext(Date publishedAt, Long id) {
        return this.findAllByIsAllowedAndPublishedAtGreaterThanEqualAndIdNotAndCanBeNearest(
            (short) 1,
            publishedAt,
            id,
            (short) 1,
            PageRequest.of(0, 3)
        );
    }

    default Page<Post> findWaitingForPublishing(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            new Sort(Sort.Direction.ASC, "publishedAt")
        );

        return this.findAllByIsAllowedAndPublishedAtGreaterThanEqual((short) 1, new Date(), pageRequest);
    }

    default Page<Post> findWaitingForPublishingAt(Date publishedAtFrom, Date publishedAtTo, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            new Sort(Sort.Direction.ASC, "publishedAt")
        );

        return this.findAllByIsAllowedAndPublishedAtBetween((short) 1,
            publishedAtFrom,
            publishedAtTo,
            pageRequest);
    }

    default Page<Post> findWaitingForModeration(Pageable pageable) {
        return this.findAllByIsAllowedAndModeratedAtIsNull((short) 0, pageable);
    }

    default void delete(Post post) {
        post.setDeletedAt(new Date());

        this.save(post);
    }
}
