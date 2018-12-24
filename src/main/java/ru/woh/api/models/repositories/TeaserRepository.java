package ru.woh.api.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Teaser;
import ru.woh.api.services.DateTimeService;

import java.util.Date;
import java.util.List;

@Repository
public interface TeaserRepository extends CrudRepository<Teaser, Teaser.TeaserPK> {
    @Query("SELECT t FROM Teaser t WHERE t.from >= ?1 AND t.to <= ?2 AND t.isTeaser = 1")
    List<Teaser> findTeasersAtDate(Date from, Date to);

    default List<Teaser> findTodayTeasers() {
        return this.findTeasersAtDate(
            DateTimeService.beginOfTheDay(new Date()),
            DateTimeService.endOfTheDay(new Date())
        );
    }

    @Query("SELECT t FROM Teaser t WHERE t.from >= ?1 AND t.to <= ?2 AND t.isTeaser = 0")
    List<Teaser> findFeaturedAtDate(Date from, Date to);

    default List<Teaser> findTodayFeatured() {
        return this.findFeaturedAtDate(
            DateTimeService.beginOfTheDay(new Date()),
            DateTimeService.endOfTheDay(new Date())
        );
    }
}
