package ru.woh.api.controllers.site;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.CommentLikesRepository;
import ru.woh.api.models.repositories.MediaRepository;
import ru.woh.api.services.CommentService;
import ru.woh.api.services.ImageStorageService;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.site.CommentView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class CommentController {
    private static final int MAX_COMMENTS = 20;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentLikesRepository commentLikesRepository;
    private final ImageStorageService imageStorageService;
    private final MediaRepository mediaRepository;

    public CommentController(
        PostService postService,
        UserService userService,
        CommentService commentService,
        CommentLikesRepository commentLikesRepository,
        ImageStorageService imageStorageService, MediaRepository mediaRepository) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.commentLikesRepository = commentLikesRepository;
        this.imageStorageService = imageStorageService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping({ "/{id:[0-9]*}/comments", "/{id:[0-9]*}/comments/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<CommentView> list(
        @PathVariable("id") Long postId,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        Post post = this.postService.one(postId);

        return this.commentService.list(post, page, MAX_COMMENTS);
    }

    @PostMapping({ "/{id:[0-9]*}/comments", "/{id:[0-9]*}/comments/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<CommentView> add(
        @PathVariable("id") Long postId,
        @RequestBody CommentView comment
    ) {
        Post post = this.postService.one(postId);

        Comment newComment = comment.model();
        newComment.setPost(post);
        newComment.setUser(this.userService.getCurrenttUser());
        if (comment.getReplyTo() != null && comment.getReplyTo().getId() != null) {
            Comment replyToComment = this.commentService.one(comment.getReplyTo().getId());
            newComment.setReplyTo(replyToComment);
        }

        processUserMedia(comment, newComment);

        this.commentService.save(newComment);

        return this.commentService.list(post, 0, MAX_COMMENTS);
    }

    private void processUserMedia(CommentView comment, Comment newComment) {
        if (comment.getMedia() != null && !comment.getMedia().isEmpty()) {
            var mediaList = comment.getMedia()
                .stream()
                .map(mediaView -> {
                    var media = new Media();
                    if (mediaView.getId() != null) {
                        media = this.mediaRepository.findById(mediaView.getId()).orElse(new Media());
                    } else {
                        media.setId(mediaView.getId());
                    }

                    media.setUrl(mediaView.getUrl());
                    media.setEmbedCode(mediaView.getEmbedCode());
                    media.setTitle(mediaView.getTitle());

                    if (mediaView.getThumbnail() != null && mediaView.getThumbnail().getContent() != null) {
                        var image = ImageStorageService.fromBase64(mediaView.getThumbnail().getContent());
                        if (image != null) {
                            var imageId = this.imageStorageService.storeBufferedImage(image,
                                String.format("comment_%d_user_%d_date_%s",
                                    comment.getId(),
                                    this.userService.getCurrenttUser().getId(),
                                    (new Date()).toString()),
                                null);

                            media.setThumbnail(imageId);
                        }
                    }

                    return this.mediaRepository.save(media);
                })
                .collect(Collectors.toSet());

            newComment.setMedia(mediaList);
        }
    }

    @PostMapping({ "/{id:[0-9]*}/comments/edit", "/{id:[0-9]*}/comments/edit/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public CommentView edit(@RequestBody CommentView comment) {
        Comment commentModel = this.commentService.one(comment.getId());

        if (!this.userService.getCurrenttUser().isModer()) {
            if (!Objects.equals(commentModel.getUser().getId(), this.userService.getCurrenttUser().getId())) {
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "you can edit only your own comments!");
            }
        }

        commentModel.setText(comment.getText());
        commentModel.setUpdatedAt(new Date());

        this.processUserMedia(comment, commentModel);

        commentModel = this.commentService.save(commentModel);

        return this.commentService.makeCommentViewWithRating(commentModel);
    }

    @PostMapping({ "/{postId:[0-9]*}/comments/delete/{id:[0-9]*}", "/{postId:[0-9]*}/comments/delete/{id:[0-9]*}/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public void delete(@PathVariable("id") Long id) {
        Comment comment = this.commentService.one(id);

        if (!this.userService.getCurrenttUser().isModer()
            && !Objects.equals(comment.getUser().getId(), this.userService.getCurrenttUser().getId())) {
            throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "you can delete only your own comments!");
        }

        this.commentService.delete(comment);
    }

    @PostMapping({ "/{postId:[0-9]*}/comments/like/{id:[0-9]*}", "/{postId:[0-9]*}/comments/like/{id:[0-9]*}/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public CommentView like(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, true);
    }

    @PostMapping({ "/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}", "/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public CommentView dislike(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, false);
    }

    private CommentView likeOrDislike(Long id, Boolean like) {
        Comment comment = this.commentService.one(id);
        User user = this.userService.getCurrenttUser();

        CommentLikes commentLikes = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
            comment.getId(),
            user.getId()
        )).orElse(null);

        if (commentLikes != null && commentLikes.getIsLike() == like) {
            this.commentLikesRepository.delete(commentLikes);

            comment.modifyRating(like ? -1 : 1);
        } else {
            if (commentLikes == null) {
                commentLikes = new CommentLikes(new CommentLikes.CommentLikesPK(comment.getId(), user.getId()), like);

                comment.modifyRating(like ? 1 : -1);
            } else {
                commentLikes.setIsLike(like);

                comment.modifyRating(like ? 2 : -2);
            }

            this.commentLikesRepository.save(commentLikes);
        }

        return this.commentService.makeCommentViewWithRating(this.commentService.save(comment));
    }
}
