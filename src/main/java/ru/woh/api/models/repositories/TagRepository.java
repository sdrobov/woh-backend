package ru.woh.api.models.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.woh.api.models.Tag;

public interface TagRepository extends PagingAndSortingRepository<Tag, Long> {
}
