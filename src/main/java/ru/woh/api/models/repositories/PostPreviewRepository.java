package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.PostPreview;
import ru.woh.api.models.Source;

import java.util.ArrayList;

@Repository
public interface PostPreviewRepository extends CrudRepository<PostPreview, Long> {
    ArrayList<PostPreview> findAllBySource(Source source);
    ArrayList<PostPreview> findAll();
}
