package ru.woh.api.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.PostPreview;
import ru.woh.api.models.Source;

@Repository
public interface PostPreviewRepository extends PagingAndSortingRepository<PostPreview, Long> {
    Page<PostPreview> findAllBySource(Source source, Pageable pageable);
}
