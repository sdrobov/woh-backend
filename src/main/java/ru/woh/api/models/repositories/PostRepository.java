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
        "LEFT JOIN Teaser t ON t.post = p AND t.from >= ?1 AND t.to <= ?2 " +
        "WHERE t.post IS NULL " +
        "AND p.isAllowed = 1 " +
        "AND p.publishedAt <= CURRENT_TIMESTAMP " +
        "ORDER BY p.publishedAt DESC")
    Page<Post> findAllNotTeasers(Date from, Date to, Pageable pageable);

    Page<Post> findAllByTags_Name(Set<String> tags, Pageable pageable);

    Page<Post> findAllByCategories_Name(Set<String> categories, Pageable pageable);

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

    Page<Post> findAllByIsAllowedTrueAndPublishedAtGreaterThanEqual(Date publishedAt, Pageable pageable);

    Page<Post> findAllByIsAllowedTrueAndPublishedAtBetween(Date from, Date to, Pageable pageable);

    Page<Post> findAllByIsAllowedFalseAndModeratedAtIsNull(Pageable pageable);

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

    default Page<Post> findAllExceptTodayTeasers(Pageable pageable) {
        return this.findAllNotTeasers(
            DateTimeService.beginOfTheDay(new Date()),
            DateTimeService.endOfTheDay(new Date()),
            pageable
        );
    }

    default Page<Post> findWaitingForPublishing(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            new Sort(Sort.Direction.ASC, "publishedAt")
        );

        return this.findAllByIsAllowedTrueAndPublishedAtGreaterThanEqual(new Date(), pageRequest);
    }

    default Page<Post> findWaitingForPublishingAt(Date publishedAtFrom, Date publishedAtTo, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            new Sort(Sort.Direction.ASC, "publishedAt")
        );

        return this.findAllByIsAllowedTrueAndPublishedAtBetween(publishedAtFrom, publishedAtTo, pageRequest);
    }

    default Page<Post> findWaitingForModeration(Pageable pageable) {
        return this.findAllByIsAllowedFalseAndModeratedAtIsNull(pageable);
    }

    default void delete(Post post) {
        post.setDeletedAt(new Date());

        this.save(post);
    }
}
