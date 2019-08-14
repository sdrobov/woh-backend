package ru.woh.api.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Post;
import ru.woh.api.models.Teaser;

import java.util.Date;
import java.util.List;

@Repository
public interface TeaserRepository extends PagingAndSortingRepository<Teaser, Teaser.TeaserPK> {
    @Query("SELECT t FROM Teaser t " +
        "INNER JOIN Post p ON t.post = p " +
        "WHERE CURRENT_TIMESTAMP BETWEEN t.from AND t.to " +
        "AND t.isTeaser = 1 " +
        "AND p.isAllowed = 1")
    List<Teaser> findActualTeasers();

    @Query("SELECT t FROM Teaser t " +
        "INNER JOIN Post p ON t.post = p " +
        "WHERE CURRENT_TIMESTAMP BETWEEN t.from AND t.to " +
        "AND t.isTeaser = 0 " +
        "AND p.isAllowed = 1")
    List<Teaser> findActualFeatured();

    List<Teaser> findAllByPost(Post post);

    List<Teaser> findAllByFromIsGreaterThanEqual(Date from);

    default List<Teaser> findFromFuture() {
        return this.findAllByFromIsGreaterThanEqual(new Date());
    }
}
