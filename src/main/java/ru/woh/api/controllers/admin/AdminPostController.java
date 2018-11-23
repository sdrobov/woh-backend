package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.admin.AdminPostView;
import ru.woh.api.views.site.PostListView;
import ru.woh.api.views.site.PostView;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminPostController {
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final static int defaultPageSize = 100;

    @Autowired
    public AdminPostController(
        UserService userService,
        PostService postService,
        PostRepository postRepository, TagRepository tagRepository
    ) {
        this.userService = userService;
        this.postService = postService;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post) {
        User user = this.userService.getCurrenttUser();

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
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView addPost(@RequestBody PostView post) {
        User user = this.userService.getCurrenttUser();

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
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public void deletePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView approvePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView dismissPost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @GetMapping("/published/")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView published(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findAllAllowed(PageRequest.of(page, AdminPostController.defaultPageSize));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/published-waiting/")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView waitingForPublish(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findWaitingForPublishing(PageRequest.of(
            page,
            AdminPostController.defaultPageSize
        ));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/published-waiting/{date:.*}/")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView waitingForPublish(
        @PathVariable("date") Date date,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        var from = Date.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"))
            .with(LocalTime.MIN)
            .toInstant(ZoneOffset.UTC));
        var to = Date.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"))
            .with(LocalTime.MIN)
            .toInstant(ZoneOffset.UTC));

        var posts = this.postRepository.findWaitingForPublishingAt(
            from,
            to,
            PageRequest.of(page, AdminPostController.defaultPageSize)
        );
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/moderation-waiting/")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView waitingForModeration(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findWaitingForModeration(PageRequest.of(
            page,
            AdminPostController.defaultPageSize
        ));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }
}
