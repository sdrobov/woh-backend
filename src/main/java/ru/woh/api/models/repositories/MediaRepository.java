package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Media;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {
}
