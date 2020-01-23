package ru.woh.api.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.models.Post;
import ru.woh.api.models.PostLikes;
import ru.woh.api.models.Role;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.PostLikesRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.site.PostListView;
import ru.woh.api.views.site.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final PostLikesRepository postLikesRepository;
    private static final int defaultPageSize = 20;

    @Autowired
    public PostController(
        PostService postService,
        PostLikesRepository postLikesRepository,
        UserService userService
    ) {
        this.postService = postService;
        this.postLikesRepository = postLikesRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return this.postService.listView(page, PostController.defaultPageSize);
    }

    @GetMapping({ "/{id:[0-9]*}", "/{id:[0-9]*}/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView one(@PathVariable("id") Long id) {
        return this.postService.view(id);
    }

    @GetMapping({ "/{id:[0-9]*}/nearest", "/{id:[0-9]*}/nearest/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<PostView> nearest(@PathVariable("id") Long id) {
        var post = this.postService.view(id);
        var posts = new ArrayList<PostView>();

        posts.addAll(this.postService.prev(post));
        posts.addAll(this.postService.next(post));

        return posts;
    }

    @PostMapping({ "/{id:[0-9]*}/like", "/{id:[0-9]*}/like/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView like(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, true);
    }


    @PostMapping({ "/{id:[0-9]*}/dislike", "/{id:[0-9]*}/dislike/" })
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView dislike(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, false);
    }

    private PostView likeOrDislike(Long id, Boolean like) {
        Post post = this.postService.one(id);
        User user = this.userService.getCurrenttUser();

        PostLikes postLike = this.postLikesRepository.findById(new PostLikes.PostLikesPK(post.getId(), user.getId()))
            .orElse(null);

        if (postLike != null && postLike.getIsLike() == like) {
            this.postLikesRepository.delete(postLike);

            post.modifyRating(like ? -1 : 1);
        } else {
            if (postLike == null) {
                postLike = new PostLikes(new PostLikes.PostLikesPK(post.getId(), user.getId()), like);

                post.modifyRating(like ? 1 : -1);
            } else {
                postLike.setIsLike(like);

                post.modifyRating(like ? 2 : -2);
            }

            this.postLikesRepository.save(postLike);
        }

        this.postService.save(post);

        return this.postService.view(id);
    }
}
