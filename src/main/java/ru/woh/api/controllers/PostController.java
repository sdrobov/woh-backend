package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.ForbiddenException;
import ru.woh.api.models.Post;
import ru.woh.api.models.PostLikes;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.PostLikesRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import javax.annotation.security.RolesAllowed;

@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostLikesRepository postLikesRepository;
    private static final int defaultPageSize = 5;

    @Autowired
    public PostController(PostService postService, PostLikesRepository postLikesRepository, UserService userService) {
        this.postService = postService;
        this.postLikesRepository = postLikesRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return this.postService.listView(page, PostController.defaultPageSize);
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id) {
        return this.postService.view(id);
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
        Integer mod = 1;

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
