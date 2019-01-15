package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.woh.api.models.PostCategory;

import java.util.List;
import java.util.Set;

@Repository
public interface PostCategoryRepository extends CrudRepository<PostCategory, PostCategory.PostCategoryPk> {
    List<PostCategory> findAllByCategoryId(Long categoryId);

    List<PostCategory> findAllByCategoryIdIn(Set<Long> categoryId);
}
