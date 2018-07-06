package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.*;
import ru.woh.api.models.*;
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

    @Autowired
    public CommentController(
        CommentRepository commentRepository,
        PostService postService,
        UserService userService
    ) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
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
        newComment.setUser(this.userService.geCurrenttUser());
        if (comment.getReplyTo() != null && comment.getReplyTo().getId() != null) {
            Comment replyToComment = this.commentRepository.findById(comment.getReplyTo().getId()).orElseThrow(() -> new NotFoundException(String.format("Reply to comment with id #%d not found", comment.getReplyTo().getId())));
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

        if (!this.userService.geCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.geCurrenttUser()) {
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

        if (!this.userService.geCurrenttUser().isModer()) {
            if (commentModel.getUser() != this.userService.geCurrenttUser()) {
                throw new ForbiddenException("you can delete only your own comments!");
            }
        }

        this.commentRepository.delete(commentModel);
    }
}
