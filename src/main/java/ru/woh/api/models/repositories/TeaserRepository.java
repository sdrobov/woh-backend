package ru.woh.api.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Teaser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Repository
public interface TeaserRepository extends CrudRepository<Teaser, Teaser.TeaserPK> {
    @Query("SELECT t FROM Teaser t WHERE t.from >= ?1 AND t.to <= ?2 AND t.isTeaser = 1")
    List<Teaser> findTeasersAtDate(Date from, Date to);

    default List<Teaser> findTodayTeasers() {
        LocalDateTime now = LocalDateTime.now();
        Date from = Date.from(now.with(LocalDateTime.MIN).toInstant(ZoneOffset.UTC));
        Date to = Date.from(now.with(LocalDateTime.MAX).toInstant(ZoneOffset.UTC));

        return this.findTeasersAtDate(from, to);
    }

    @Query("SELECT t FROM Teaser t WHERE t.from >= ?1 AND t.to <= ?2 AND t.isTeaser = 0")
    List<Teaser> findFeaturedAtDate(Date from, Date to);

    default List<Teaser> findTodayFeatured() {
        LocalDateTime now = LocalDateTime.now();
        Date from = Date.from(now.with(LocalDateTime.MIN).toInstant(ZoneOffset.UTC));
        Date to = Date.from(now.with(LocalDateTime.MAX).toInstant(ZoneOffset.UTC));

        return this.findFeaturedAtDate(from, to);
    }
}
