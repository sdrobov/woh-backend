package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.ForbiddenException;
import ru.woh.api.NotFoundException;
import ru.woh.api.UserService;
import ru.woh.api.models.*;
import ru.woh.api.views.CommentView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class CommentController {
    private static final int MAX_COMMENTS = 100;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

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
    public List<CommentView> add(@PathVariable("id") Long postId, @RequestParam(value = "comment") CommentView comment, HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null) {
            throw new ForbiddenException();
        }

        PostModel post = this.postRepository.findOne(postId);
        if (post == null) {
            throw new NotFoundException();
        }

        CommentModel newComment = CommentModel.fromView(comment);
        newComment.setPost(post);
        newComment.setUser(user);
        this.commentRepository.save(newComment);

        return this.commentRepository
            .findAllByPost(post, new PageRequest(0, MAX_COMMENTS))
            .map(CommentModel::view)
            .getContent();
    }
}
