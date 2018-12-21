package ru.woh.api.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
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
    private static final int defaultPageSize = 5;

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

    @GetMapping({"/by-tag/{tag:.*}", "/by-tag/{tag:.*}/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView byTag(
        @PathVariable("tag") String tag,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return this.postService.byTag(page, PostController.defaultPageSize, tag);
    }

    @GetMapping({"/{id:[0-9]*}", "/{id:[0-9]*}/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView one(@PathVariable("id") Long id) {
        return this.postService.view(id);
    }

    @GetMapping({"/{id:[0-9]*}/nearest", "/{id:[0-9]*}/nearest/"})
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<PostView> nearest(@PathVariable("id") Long id) {
        var post = this.postService.view(id);
        var posts = new ArrayList<PostView>();

        posts.addAll(this.postService.prev(post));
        posts.addAll(this.postService.next(post));

        return posts;
    }

    @PostMapping({"/{id:[0-9]*}/like", "/{id:[0-9]*}/like/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView like(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, true);
    }


    @PostMapping({"/{id:[0-9]*}/dislike", "/{id:[0-9]*}/dislike/"})
    @RolesAllowed({ Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostView dislike(@PathVariable("id") Long id) {
        return this.likeOrDislike(id, false);
    }

    @SuppressWarnings("Duplicates")
    private PostView likeOrDislike(Long id, Boolean like) {
        Post post = this.postService.one(id);
        User user = this.userService.getCurrenttUser();
        int mod = 1;

        PostLikes postLike = this.postLikesRepository.findById(new PostLikes.PostLikesPK(post.getId(), user.getId()))
            .orElse(null);

        if (postLike != null) {
            if (postLike.getIsLike() == like) {
                throw new HttpClientErrorException(HttpStatus.FORBIDDEN, String.format("you can only %s once", like ? "like" : "dislike"));
            }

            postLike.setIsLike(like);
            mod += 1;
        } else {
            postLike = new PostLikes(new PostLikes.PostLikesPK(post.getId(), user.getId()), like);
        }

        this.postLikesRepository.save(postLike);

        post.setRating((post.getRating() != null ? post.getRating() : 0) + (like ? mod : 0 - mod));
        this.postService.save(post);

        return this.postService.view(id);
    }
}
