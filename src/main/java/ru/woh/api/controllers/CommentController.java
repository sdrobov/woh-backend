package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.CommentRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.CommentView;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed({Role.USER, Role.MODER, Role.ADMIN})
    public List<CommentView> add(
        @PathVariable("id") Long postId,
        @RequestBody CommentView comment
    ) {
        Post post = this.postService.one(postId);

        Comment newComment = comment.model();
        newComment.setPost(post);
        newComment.setUser(this.userService.geCurrenttUser());
        this.commentRepository.save(newComment);

        return this.commentRepository
            .findAllByPost(post, PageRequest.of(0, MAX_COMMENTS))
            .map(Comment::view)
            .getContent();
    }
}
