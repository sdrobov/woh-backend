package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.*;
import ru.woh.api.views.site.PostListView;
import ru.woh.api.views.site.PostView;
import ru.woh.api.views.site.RatingView;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final PostLikesRepository postLikesRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    @Autowired
    public PostService(
        PostRepository postRepository,
        UserService userService,
        CommentService commentService,
        PostLikesRepository postLikesRepository,
        TagRepository tagRepository,
        CategoryRepository categoryRepository,
        PostCategoryRepository postCategoryRepository
    ) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;
        this.postLikesRepository = postLikesRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.postCategoryRepository = postCategoryRepository;
    }

    private Page<Post> list(Integer page, Integer limit) {
        User user = this.userService.getCurrenttUser();
        boolean isModer = user != null && user.isModer();

        return (
            isModer
                ? this.postRepository.findAll(PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "createdAt")))
                : this.postRepository.findAllExceptTodayTeasers(PageRequest.of(
                    page,
                    limit,
                    new Sort(Sort.Direction.DESC, "createdAt")
                ))
        );
    }

    public Post one(Long id) {
        return this.postRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
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
        var posts = this.postRepository.findAllByTags_Name(
            Collections.singleton(tag),
            PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "publishedAt"))
        );
        var views = posts.getContent().stream().map(this::makeViewWithRating).collect(Collectors.toList());

        return new PostListView(posts.getTotalElements(), posts.getTotalPages(), page, views);
    }

    public PostListView byCategory(Integer page, Integer limit, String category) {
        var categories = Set.of(category.split(","));
        Page<Post> posts;

        if (categories.size() > 1) {
            var categoryIds = this.categoryRepository.findAllByNameIn(categories)
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());

            var postCategories = new HashMap<Long, List<Long>>();
            this.postCategoryRepository.findAllByCategoryIdIn(categoryIds)
                .forEach(postCategory -> {
                    postCategories.computeIfPresent(postCategory.getPostId(), (postId, currentPostCategories) -> {
                        if (!currentPostCategories.contains(postCategory.getCategoryId())) {
                            currentPostCategories.add(postCategory.getCategoryId());
                        }

                        return currentPostCategories;
                    });

                    postCategories.computeIfAbsent(
                        postCategory.getPostId(),
                        postId -> {
                            List<Long> currentPostCategories = new ArrayList<>();
                            currentPostCategories.add(postCategory.getCategoryId());

                            return currentPostCategories;
                        }
                    );
                });

            List<Long> postIds = new ArrayList<>();
            postCategories.forEach((postId, categoriesId) -> {
                if (categoriesId.containsAll(categoryIds)) {
                    postIds.add(postId);
                }
            });

            posts = this.postRepository.findAllByIdIn(
                postIds,
                PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "publishedAt"))
            );
        } else {
            posts = this.postRepository.findAllByCategories_NameIn(
                categories,
                PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "publishedAt"))
            );
        }

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

    public Post updateWithView(Post post, PostView postView) {
        post.setTitle(postView.getTitle());
        post.setText(postView.getText());
        post.setSource(postView.getSource());
        post.setAnnounce(postView.getAnnounce());

        if (postView.getTags() != null) {
            post.setTags(postView.getTags()
                .stream()
                .map(String::trim)
                .distinct()
                .map(tagName -> this.tagRepository.findFirstByName(tagName).orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);

                    return this.tagRepository.save(tag);
                }))
                .collect(Collectors.toSet()));
        }

        if (postView.getCategories() != null) {
            post.setCategories(postView.getCategories()
                .stream()
                .map(String::trim)
                .distinct()
                .map(categoryName -> this.categoryRepository.findFirstByName(categoryName).orElseGet(() -> {
                    Category category = new Category();
                    category.setName(categoryName);

                    return this.categoryRepository.save(category);
                }))
                .collect(Collectors.toSet()));
        }

        return post;
    }
}
