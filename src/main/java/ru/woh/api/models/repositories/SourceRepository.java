package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.woh.api.models.Source;

public interface SourceRepository extends PagingAndSortingRepository<Source, Long> {
    Page<Source> findAll();
}
