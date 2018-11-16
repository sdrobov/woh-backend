package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.ForbiddenException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.PostLikesRepository;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostLikesRepository postLikesRepository;
    private final TagRepository tagRepository;
    private static final int defaultPageSize = 5;

    @Autowired
    public PostController(
        PostService postService,
        PostLikesRepository postLikesRepository,
        UserService userService,
        TagRepository tagRepository
    ) {
        this.postService = postService;
        this.postLikesRepository = postLikesRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/")
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return this.postService.listView(page, PostController.defaultPageSize);
    }

    @GetMapping("/{id:[0-9]*}")
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView one(@PathVariable("id") Long id) {
        return this.postService.view(id);
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
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
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
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
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public void deletePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView approvePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView dismissPost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/like")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public PostView like(@PathVariable("id") Long id) {
        Post post = this.postService.one(id);
        User user = this.userService.getCurrenttUser();
        int mod = 1;

        PostLikes postLike = this.postLikesRepository.findById(new PostLikes.PostLikesPK(post.getId(), user.getId()))
            .orElse(null);

        if (postLike != null) {
            if (postLike.getIsLike()) {
                throw new ForbiddenException("you can only like once");
            }

            postLike.setIsLike(true);
            mod += 1;
        } else {
            postLike = new PostLikes(new PostLikes.PostLikesPK(post.getId(), user.getId()), true);
        }

        this.postLikesRepository.save(postLike);

        post.setRating((post.getRating() != null ? post.getRating() : 0) + mod);
        this.postService.save(post);

        return this.postService.view(id);
    }


    @PostMapping("/{id:[0-9]*}/dislike")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN})
    public PostView dislike(@PathVariable("id") Long id) {
        Post post = this.postService.one(id);
        User user = this.userService.getCurrenttUser();
        int mod = 1;

        PostLikes postLike = this.postLikesRepository.findById(new PostLikes.PostLikesPK(post.getId(), user.getId()))
            .orElse(null);

        if (postLike != null) {
            if (!postLike.getIsLike()) {
                throw new ForbiddenException("you can only dislike once");
            }

            postLike.setIsLike(false);
            mod += 1;
        } else {
            postLike = new PostLikes(new PostLikes.PostLikesPK(post.getId(), user.getId()), false);
        }

        this.postLikesRepository.save(postLike);

        post.setRating((post.getRating() != null ? post.getRating() : 0) - mod);
        this.postService.save(post);

        return this.postService.view(id);
    }
}
