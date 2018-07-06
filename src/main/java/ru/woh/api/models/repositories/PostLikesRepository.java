package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.woh.api.models.PostLikes;

public interface PostLikesRepository extends CrudRepository<PostLikes, PostLikes.PostLikesPK> {
}
