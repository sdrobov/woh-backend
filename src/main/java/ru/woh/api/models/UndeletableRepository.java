package ru.woh.api.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@NoRepositoryBean
interface UndeletableRepository<T extends UndeletableEntity, Id extends Long> extends PagingAndSortingRepository<T, Id> {
    @Override
    @Query("SELECT e FROM #{#entityName} e WHERE e.deletedAt IS NULL ORDER BY e.createdAt DESC")
    Page<T> findAll(Pageable pageable);

    @Override
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = ?1")
    public void delete(Long id);

    @Override
    @Transactional
    public default void delete(T entity) {
        delete(entity.getId());
    }

    @Override
    public default void delete(Iterable<? extends T> iterable) {
        iterable.forEach(entity -> delete(entity.getId()));
    }

    @Override
    public boolean exists(Long aLong);

    @Override
    public long count();
}
