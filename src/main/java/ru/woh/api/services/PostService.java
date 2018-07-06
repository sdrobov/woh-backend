package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.models.Post;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.models.User;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    private List<Post> list(Integer page, Integer limit) {
        User user = this.userService.getCurrenttUser();
        Boolean isModer = user != null && user.isModer();

        return (
            isModer
                ? this.postRepository.findAll(PageRequest.of(page, limit, new Sort(Sort.Direction.DESC, "createdAt")))
                : this.postRepository.findAllByIsAllowedTrue(PageRequest.of(
                    page,
                    limit,
                    new Sort(Sort.Direction.DESC, "createdAt")
                ))
        ).getContent();
    }

    public Post one(Long id) {
        return this.postRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("post #%d not found", id)));
    }

    public PostView view(Long id) {
        Post post = this.one(id);
        User user = this.userService.getCurrenttUser();
        Boolean isModer = user != null && user.isModer();

        return isModer ? post.adminView() : post.view();
    }

    public PostListView listView(Integer page, Integer limit) {
        User user = this.userService.getCurrenttUser();
        Boolean isModer = user != null && user.isModer();

        return new PostListView(this.list(page, limit)
            .stream()
            .map(isModer ? Post::adminView : Post::view)
            .collect(Collectors.toList()));
    }

    public Post save(Post post) {
        return this.postRepository.save(post);
    }

    public void delete(Post post) {
        this.postRepository.delete(post);
    }
}
