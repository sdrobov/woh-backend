package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.services.DateTimeService;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.admin.AdminPostView;
import ru.woh.api.views.site.ListView;
import ru.woh.api.views.site.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminPostController {
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;
    private final static int defaultPageSize = 20;

    @Autowired
    public AdminPostController(
        UserService userService,
        PostService postService,
        PostRepository postRepository
    ) {
        this.userService = userService;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView postView) {
        User user = this.userService.getCurrenttUser();

        Post post = this.postService.one(id);

        post.setModeratedAt(new Date());
        post.setUpdatedAt(new Date());
        post.setModerator(user);

        return this.postService.save(this.postService.updateWithView(post, postView)).adminView();
    }

    @PostMapping("/add")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public AdminPostView addPost(@RequestBody PostView postView) {
        User user = this.userService.getCurrenttUser();
        Post post = new Post();

        post.setCreatedAt(new Date());
        post.setModeratedAt(new Date());
        post.setModerator(user);
        post.setIsAllowed((short) 1);

        return this.postService.save(this.postService.updateWithView(post, postView)).adminView();
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

    @GetMapping("/published")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ListView<PostView> published(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findAllAllowed(PageRequest.of(page, AdminPostController.defaultPageSize));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new ListView<>(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/published-waiting")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ListView<PostView> waitingForPublish(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findWaitingForPublishing(PageRequest.of(
            page,
            AdminPostController.defaultPageSize
        ));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new ListView<>(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/published-waiting/{date:.*}")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ListView<PostView> waitingForPublish(
        @PathVariable("date") Date date,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        var posts = this.postRepository.findWaitingForPublishingAt(
            DateTimeService.beginOfTheDay(date),
            DateTimeService.endOfTheDay(date),
            PageRequest.of(page, AdminPostController.defaultPageSize)
        );
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new ListView<>(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    @GetMapping("/moderation-waiting")
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ListView<PostView> waitingForModeration(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var posts = this.postRepository.findWaitingForModeration(PageRequest.of(
            page,
            AdminPostController.defaultPageSize
        ));
        var views = posts.getContent().stream().map(Post::view).collect(Collectors.toList());

        return new ListView<>(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }
}
