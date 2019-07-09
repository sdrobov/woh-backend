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
    @Query("SELECT t FROM Teaser t WHERE ?1 BETWEEN t.from AND t.to AND t.isTeaser = 1")
    List<Teaser> findActualTeasers(Date now);

    @Query("SELECT t FROM Teaser t WHERE ?1 BETWEEN t.from AND t.to AND t.isTeaser = 0")
    List<Teaser> findActualFeatured(Date now);

    List<Teaser> findAllByPost(Post post);
}
