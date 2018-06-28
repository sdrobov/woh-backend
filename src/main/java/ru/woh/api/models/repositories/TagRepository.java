package ru.woh.api.models.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.woh.api.models.Tag;

import java.util.Optional;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
    Optional<Tag> findFirstByName(String name);
}
