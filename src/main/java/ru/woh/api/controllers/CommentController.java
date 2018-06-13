package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.NotFoundException;
import ru.woh.api.models.*;
import ru.woh.api.views.CommentView;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CommentController extends BaseRestController {
    private static final int MAX_COMMENTS = 100;

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id:[0-9]*}/comments")
    public List<CommentView> list(
        @PathVariable("id") Long postId,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        PostModel post = this.postRepository.findById(postId).orElseThrow(NotFoundException::new);

        return this.commentRepository
            .findAllByPost(post, PageRequest.of(page, MAX_COMMENTS))
            .map(CommentModel::view)
            .getContent();
    }

    @PostMapping("/{id:[0-9]*}/comments")
    @RolesAllowed({RoleModel.USER, RoleModel.MODER, RoleModel.ADMIN})
    public List<CommentView> add(
        @PathVariable("id") Long postId,
        @RequestBody CommentView comment,
        HttpServletRequest request
    ) {
        this.needAuth(request);

        PostModel post = this.postRepository.findById(postId).orElseThrow(NotFoundException::new);

        CommentModel newComment = comment.model();
        newComment.setPost(post);
        newComment.setUser(this.getUser(request));
        this.commentRepository.save(newComment);

        return this.commentRepository
            .findAllByPost(post, PageRequest.of(0, MAX_COMMENTS))
            .map(CommentModel::view)
            .getContent();
    }
}
