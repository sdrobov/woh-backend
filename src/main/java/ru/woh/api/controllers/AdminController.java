package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.CommentRepository;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TagRepository tagRepository;

    @Autowired
    public AdminController(
        CommentRepository commentRepository,
        UserService userService,
        PostService postService,
        TagRepository tagRepository
    ) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post) {
        User user = this.userService.geCurrenttUser();

        Post postModel = this.postService.one(id);

        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setModeratedAt(new Date());
        postModel.setUpdatedAt(new Date());
        postModel.setModerator(user);
        postModel.setAnnounce(post.getAnnounce());
        if (post.getTags() != null) {
            postModel.setTags(
                post.getTags()
                    .stream()
                    .map(String::trim)
                    .distinct()
                    .map(tagName -> this.tagRepository.findFirstByName(tagName).orElseGet(() -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }))
                    .collect(Collectors.toSet())
            );
        }

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
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
        postModel.setAnnounce(post.getAnnounce());
        if (post.getTags() != null) {
            postModel.setTags(
                post.getTags()
                    .stream()
                    .map(String::trim)
                    .distinct()
                    .map(tagName -> this.tagRepository.findFirstByName(tagName).orElseGet(() -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }))
                    .collect(Collectors.toSet())
            );
        }

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public void delete(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView approve(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.geCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView dismiss(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.geCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }
}
