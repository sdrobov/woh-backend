package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.models.Post;
import ru.woh.api.models.PostLikes;
import ru.woh.api.models.User;
import ru.woh.api.models.repositories.PostLikesRepository;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.views.site.PostListView;
import ru.woh.api.views.site.PostView;
import ru.woh.api.views.site.RatingView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final PostLikesRepository postLikesRepository;

    @Autowired
    public PostService(
        PostRepository postRepository,
        UserService userService,
        CommentService commentService,
        PostLikesRepository postLikesRepository
    ) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.postLikesRepository = postLikesRepository;
    }

    private Page<Post> list(Integer page, Integer limit) {
        User user = this.userService.getCurrenttUser();
        boolean isModer = user != null && user.isModer();

        return (
            isModer
                ? this.postRepository.findAll(PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "createdAt")))
                : this.postRepository.findAllAllowed(PageRequest.of(
                    page,
                    limit,
                    new Sort(Sort.Direction.DESC, "createdAt")
                ))
        );
    }

    public Post one(Long id) {
        return this.postRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "post #%d not found",
            id
        )));
    }

    public PostView view(Long id) {
        return this.makeViewWithRating(this.one(id));
    }

    public List<PostView> prev(PostView post) {
        return this.postRepository.findPrev(post.getPublishedAt(), post.getId()).stream().map(Post::view).collect(
            Collectors.toList());
    }

    public List<PostView> next(PostView post) {
        return this.postRepository.findNext(post.getPublishedAt(), post.getId()).stream().map(Post::view).collect(
            Collectors.toList());
    }

    public PostListView listView(Integer page, Integer limit) {
        var postPage = this.list(page, limit);
        var views = postPage.getContent()
            .stream()
            .map(this::makeViewWithRating)
            .collect(Collectors.toList());

        return new PostListView(postPage.getTotalElements(), postPage.getTotalPages(), page, views);
    }

    public PostListView byTag(Integer page, Integer limit, String tag) {
        var posts = this.postRepository.findAllByTags(
            Collections.singleton(tag),
            PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "createdAt"))
        );
        var views = posts.getContent().stream().map(this::makeViewWithRating).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    private PostView makeViewWithRating(Post post) {
        User user = this.userService.getCurrenttUser();
        Boolean isModer = user != null && user.isModer();
        PostView view = isModer ? post.adminView() : post.view();
        RatingView rating = new RatingView();

        rating.setCount(post.getRating());

        if (user != null) {
            PostLikes like = this.postLikesRepository.findById(new PostLikes.PostLikesPK(post.getId(), user.getId()))
                .orElse(null);

            if (like != null) {
                rating.setLike(like.getIsLike());
                rating.setDislike(!like.getIsLike());
            }
        }

        view.setRating(rating);
        view.setComments(this.commentService.last(post));
        view.setTotalComments(this.commentService.count(post));

        return view;
    }

    public Post save(Post post) {
        return this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }
}
