package ru.woh.api.models.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    Optional<Tag> findFirstByName(String name);
    List<Tag> findAll();
}
