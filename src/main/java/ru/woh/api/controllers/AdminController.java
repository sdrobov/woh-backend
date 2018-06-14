package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.CommentRepository;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.CommentView;
import ru.woh.api.views.PostView;
import ru.woh.api.views.TagView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Autowired
    public AdminController(
        CommentRepository commentRepository,
        UserService userService,
        PostService postService
    ) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post) {
        User user = this.userService.geCurrenttUser();

        Post postModel = this.postService.one(id);

        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setModeratedAt(new Date());
        postModel.setUpdatedAt(new Date());
        postModel.setModerator(user);
        postModel.setTags(
            post.getTags()
                .stream()
                .map(TagView::model)
                .collect(Collectors.toSet())
        );

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public AdminPostView add(@RequestBody PostView post) {
        User user = this.userService.geCurrenttUser();

        Post postModel = new Post();
        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setCreatedAt(new Date());
        postModel.setModeratedAt(new Date());
        postModel.setModerator(user);
        postModel.setIsAllowed(true);
        postModel.setTags(
            post.getTags()
                .stream()
                .map(TagView::model)
                .collect(Collectors.toSet())
        );

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public void delete(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public AdminPostView approve(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.geCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public AdminPostView dismiss(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.geCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/comments/edit/")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public CommentView editComment(@RequestBody CommentView comment) {
        Comment commentModel = this.commentRepository.findById(comment.getId())
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", comment.getId())));

        commentModel.setText(comment.getText());
        commentModel.setUpdatedAt(new Date());

        commentModel = this.commentRepository.save(commentModel);

        return commentModel.view();
    }

    @PostMapping("/{postId:[0-9]*}/comments/delete/{id:[0-9]*}")
    @RolesAllowed({Role.MODER, Role.ADMIN})
    public void deleteComment(@PathVariable("id") Long id) {
        Comment commentModel = this.commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", id)));

        this.commentRepository.delete(commentModel);
    }
}
