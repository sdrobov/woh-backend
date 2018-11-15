package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.woh.api.models.Source;

import java.util.ArrayList;

public interface SourceRepository extends CrudRepository<Source, Long> {
    ArrayList<Source> findAll();
}
