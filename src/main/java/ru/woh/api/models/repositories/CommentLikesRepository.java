package ru.woh.api.models.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.woh.api.models.CommentLikes;

public interface CommentLikesRepository extends CrudRepository<CommentLikes, CommentLikes.CommentLikesPK> {
}
