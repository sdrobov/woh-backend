package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.Comment;
import ru.woh.api.models.CommentLikes;
import ru.woh.api.models.Media;
import ru.woh.api.models.Post;
import ru.woh.api.models.repositories.CommentLikesRepository;
import ru.woh.api.models.repositories.CommentRepository;
import ru.woh.api.views.site.CommentView;
import ru.woh.api.views.site.RatingView;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final UserService userService;
    private final GridFsService gridFsService;

    @Autowired
    public CommentService(
        CommentRepository commentRepository,
        CommentLikesRepository commentLikesRepository,
        UserService userService,
        GridFsService gridFsService) {
        this.commentRepository = commentRepository;
        this.commentLikesRepository = commentLikesRepository;
        this.userService = userService;
        this.gridFsService = gridFsService;
    }


    public Comment one(Long id) {
        return this.commentRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
            "Comment #%d not found",
            id
        )));
    }

    List<CommentView> last(Post post) {
        return this.commentRepository.findAllByPost(
            post,
            PageRequest.of(0, 2, Sort.by(Sort.Order.desc("createdAt")))
        ).map(this::makeCommentViewWithRating).getContent();
    }

    Long count(Post post) {
        return this.commentRepository.countByPost(post);
    }

    public List<CommentView> list(Post post, Integer page, Integer size) {
        return this.commentRepository.findAllByPost(
            post,
            PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")))
        ).map(this::makeCommentViewWithRating).getContent();
    }

    public CommentView makeCommentViewWithRating(Comment comment) {
        CommentView view = comment.view();
        if (comment.getMedia() != null && !comment.getMedia().isEmpty()) {
            view.setMedia(
                comment.getMedia()
                    .stream()
                    .map(Media::view)
                    .peek(mediaView -> {
                        if (mediaView.getThumbnail() != null
                            && mediaView.getThumbnail().getUrl() != null) {
                            var image = this.gridFsService.findById(mediaView.getThumbnail().getUrl());

                            if (image != null) {
                                var meta = image.getMetadata();

                                if (meta != null) {
                                    try {
                                        Integer width = Integer.valueOf(meta.getString("width"));
                                        Integer height = Integer.valueOf(meta.getString("height"));

                                        mediaView.getThumbnail().setWidth(width);
                                        mediaView.getThumbnail().setHeight(height);
                                    } catch (NumberFormatException ignored) {}
                                }

                                mediaView.getThumbnail().setUrl("/image/" + mediaView.getThumbnail().getUrl());
                            } else {
                                mediaView.setUrl(null);
                            }
                        }
                    })
                    .collect(Collectors.toList())
            );
        }

        RatingView rating = new RatingView();

        rating.setCount(comment.getRating());
        if (this.userService.getCurrenttUser() != null) {
            CommentLikes like = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
                comment.getId(),
                this.userService.getCurrenttUser().getId()
            )).orElse(null);

            if (like != null) {
                rating.setLike(like.getIsLike());
                rating.setDislike(!like.getIsLike());
            }
        }

        view.setRating(rating);

        return view;
    }

    public Comment save(Comment comment) {
        return this.commentRepository.save(comment);
    }

    public void delete(Comment comment) {
        this.commentRepository.delete(comment);
    }
}
