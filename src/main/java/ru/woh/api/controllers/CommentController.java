package ru.woh.api.controllers;

import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.ForbiddenException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.CommentLikesRepository;
import ru.woh.api.services.CommentService;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.CommentView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;

@RestController
public class CommentController {
    private static final int MAX_COMMENTS = 100;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentLikesRepository commentLikesRepository;

    public CommentController(
        PostService postService,
        UserService userService,
        CommentService commentService,
        CommentLikesRepository commentLikesRepository
    ) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.commentLikesRepository = commentLikesRepository;
    }

    @GetMapping("/{id:[0-9]*}/comments")
    public List<CommentView> list(
        @PathVariable("id") Long postId,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        Post post = this.postService.one(postId);

        return this.commentService.list(post, page, MAX_COMMENTS);
    }

    @PostMapping("/{id:[0-9]*}/comments")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
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

        this.commentService.save(newComment);

        return this.commentService.list(post, 0, MAX_COMMENTS);
    }


    @PostMapping("/{id:[0-9]*}/comments/edit/")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView editComment(@RequestBody CommentView comment) {
        Comment commentModel = this.commentService.one(comment.getId());

        if (!this.userService.getCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.getCurrenttUser()) {
                throw new ForbiddenException("you can delete only your own comments!");
            }
        }

        commentModel.setText(comment.getText());
        commentModel.setUpdatedAt(new Date());

        commentModel = this.commentService.save(commentModel);

        return this.commentService.makeCommentViewWithRating(commentModel);
    }

    @PostMapping("/{postId:[0-9]*}/comments/delete/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public void deleteComment(@PathVariable("id") Long id) {
        Comment commentModel = this.commentService.one(id);

        if (!this.userService.getCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.getCurrenttUser()) {
                throw new ForbiddenException("you can delete only your own comments!");
            }
        }

        this.commentService.delete(commentModel);
    }

    @PostMapping("/{postId:[0-9]*}/comments/like/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView like(@PathVariable("id") Long id) {
        Comment comment = this.commentService.one(id);
        User user = this.userService.getCurrenttUser();
        Integer mod = 1;

        CommentLikes commentLikes = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
            comment.getId(),
            user.getId()
        )).orElse(null);

        if (commentLikes != null) {
            if (commentLikes.getIsLike()) {
                throw new ForbiddenException("you can only like once");
            }

            commentLikes.setIsLike(true);
            mod += 1;
        } else {
            commentLikes = new CommentLikes(new CommentLikes.CommentLikesPK(comment.getId(), user.getId()), true);
        }

        this.commentLikesRepository.save(commentLikes);

        comment.setRating((comment.getRating() != null ? comment.getRating() : 0) + mod);
        comment = this.commentService.save(comment);

        return this.commentService.makeCommentViewWithRating(comment);
    }

    @PostMapping("/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView dislike(@PathVariable("id") Long id) {
        Comment comment = this.commentService.one(id);
        User user = this.userService.getCurrenttUser();
        Integer mod = 1;

        CommentLikes commentLikes = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
            comment.getId(),
            user.getId()
        )).orElse(null);

        if (commentLikes != null) {
            if (!commentLikes.getIsLike()) {
                throw new ForbiddenException("you can only dislike once");
            }

            commentLikes.setIsLike(false);
            mod += 1;
        } else {
            commentLikes = new CommentLikes(new CommentLikes.CommentLikesPK(comment.getId(), user.getId()), false);
        }

        this.commentLikesRepository.save(commentLikes);

        comment.setRating((comment.getRating() != null ? comment.getRating() : 0) - mod);
        comment = this.commentService.save(comment);

        return this.commentService.makeCommentViewWithRating(comment);
    }
}
