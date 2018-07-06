package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.ForbiddenException;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.CommentLikesRepository;
import ru.woh.api.models.repositories.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentLikesRepository commentLikesRepository;

    @Autowired
    public CommentController(
        CommentRepository commentRepository,
        PostService postService,
        UserService userService,
        CommentLikesRepository commentLikesRepository
    ) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
        this.commentLikesRepository = commentLikesRepository;
    }

    @GetMapping("/{id:[0-9]*}/comments")
    public List<CommentView> list(
        @PathVariable("id") Long postId,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        Post post = this.postService.one(postId);

        return this.commentRepository
            .findAllByPost(post, PageRequest.of(page, MAX_COMMENTS))
            .map(Comment::view)
            .getContent();
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
            Comment replyToComment = this.commentRepository.findById(comment.getReplyTo().getId())
                .orElseThrow(() -> new NotFoundException(String.format(
                    "Reply to comment with id #%d not found",
                    comment.getReplyTo().getId()
                )));
            newComment.setReplyTo(replyToComment);
        }

        this.commentRepository.save(newComment);

        return this.commentRepository
            .findAllByPost(post, PageRequest.of(0, MAX_COMMENTS))
            .map(Comment::view)
            .getContent();
    }


    @PostMapping("/{id:[0-9]*}/comments/edit/")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView editComment(@RequestBody CommentView comment) {
        Comment commentModel = this.commentRepository.findById(comment.getId())
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", comment.getId())));

        if (!this.userService.getCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.getCurrenttUser()) {
                throw new ForbiddenException("you can delete only your own comments!");
            }
        }

        commentModel.setText(comment.getText());
        commentModel.setUpdatedAt(new Date());

        commentModel = this.commentRepository.save(commentModel);

        return commentModel.view();
    }

    @PostMapping("/{postId:[0-9]*}/comments/delete/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public void deleteComment(@PathVariable("id") Long id) {
        Comment commentModel = this.commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", id)));

        if (!this.userService.getCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.getCurrenttUser()) {
                throw new ForbiddenException("you can delete only your own comments!");
            }
        }

        this.commentRepository.delete(commentModel);
    }

    @PostMapping("/{postId:[0-9]*}/comments/like/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView like(@PathVariable("id") Long id) {
        Comment comment = this.commentRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "comment #%d not found",
            id
        )));
        User user = this.userService.getCurrenttUser();
        Integer mod = 1;

        CommentLikes commentLikes = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
            comment.getId(),
            user.getId()
        ))
            .orElse(null);

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
        comment = this.commentRepository.save(comment);

        return comment.view();
    }

    @PostMapping("/{postId:[0-9]*}/comments/dislike/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public CommentView dislike(@PathVariable("id") Long id) {
        Comment comment = this.commentRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "comment #%d not found",
            id
        )));
        User user = this.userService.getCurrenttUser();
        Integer mod = 1;

        CommentLikes commentLikes = this.commentLikesRepository.findById(new CommentLikes.CommentLikesPK(
            comment.getId(),
            user.getId()
        ))
            .orElse(null);

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
        comment = this.commentRepository.save(comment);

        return comment.view();
    }
}
