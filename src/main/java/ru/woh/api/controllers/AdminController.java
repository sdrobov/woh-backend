package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.NotFoundException;
import ru.woh.api.models.*;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.CommentView;
import ru.woh.api.views.PostView;
import ru.woh.api.views.TagView;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminController extends BaseRestController {
    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public AdminController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post, HttpServletRequest request) {
        this.needModer(request);
        UserModel user = this.getUser(request);

        PostModel postModel = this.postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("post #%d not found", id)));

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

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public AdminPostView add(@RequestBody PostView post, HttpServletRequest request) {
        this.needModer(request);
        UserModel user = this.getUser(request);

        PostModel postModel = new PostModel();
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

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("post #%d not found", id)));

        this.postRepository.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public AdminPostView approve(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("post #%d not found", id)));

        postModel.approve(this.getUser(request));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public AdminPostView dismiss(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("post #%d not found", id)));

        postModel.dismiss(this.getUser(request));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/comments/edit/")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public CommentView editComment(@RequestBody CommentView comment, HttpServletRequest request) {
        this.needModer(request);

        CommentModel commentModel = this.commentRepository.findById(comment.getId())
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", comment.getId())));

        commentModel.setText(comment.getText());
        commentModel.setUpdatedAt(new Date());

        commentModel = this.commentRepository.save(commentModel);

        return commentModel.view();
    }

    @PostMapping("/{postId:[0-9]*}/comments/delete/{id:[0-9]*}")
    @RolesAllowed({RoleModel.MODER, RoleModel.ADMIN})
    public void deleteComment(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        CommentModel commentModel = this.commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("comment #%d not found", id)));

        this.commentRepository.delete(commentModel);
    }
}
