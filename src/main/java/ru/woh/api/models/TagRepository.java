package ru.woh.api.models;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TagRepository extends PagingAndSortingRepository<TagModel, Long> {
}
