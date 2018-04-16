package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.NotFoundException;
import ru.woh.api.models.*;
import ru.woh.api.views.CommentView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController extends BaseRestController {
    private static final int MAX_COMMENTS = 100;

    protected final PostRepository postRepository;

    protected final CommentRepository commentRepository;

    @Autowired
    public CommentController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id:[0-9]*}/comments")
    public List<CommentView> list(@PathVariable("id") Long postId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        PostModel post = this.postRepository.findOne(postId);
        if (post == null) {
            throw new NotFoundException();
        }

        return this.commentRepository
            .findAllByPost(post, new PageRequest(page, MAX_COMMENTS))
            .map(CommentModel::view)
            .getContent();
    }

    @PostMapping("/{id:[0-9]*}/comments")
    public List<CommentView> add(@PathVariable("id") Long postId, @RequestBody CommentView comment, HttpSession session) {
        this.needAuth(session);

        PostModel post = this.postRepository.findOne(postId);
        if (post == null) {
            throw new NotFoundException();
        }

        CommentModel newComment = CommentModel.fromView(comment);
        newComment.setPost(post);
        newComment.setUser(this.getUser(session));
        this.commentRepository.save(newComment);

        return this.commentRepository
            .findAllByPost(post, new PageRequest(0, MAX_COMMENTS))
            .map(CommentModel::view)
            .getContent();
    }
}
