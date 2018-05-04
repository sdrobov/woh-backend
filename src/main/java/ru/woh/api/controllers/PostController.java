package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.ForbiddenException;
import ru.woh.api.NotFoundException;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController extends BaseRestController {
    private final PostRepository postRepository;
    private static final int defaultPageSize = 100;
    private static final Sort sort = new Sort(Sort.Direction.DESC, "createdAt");

    @Autowired public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public PostListView list(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        HttpServletRequest request
    ) {
        Boolean isModer = false;
        try {
            this.needModer(request);
            isModer = true;
        } catch (ForbiddenException e) {/* nop */}
        return new PostListView((isModer
            ? this.postRepository.findAll(new PageRequest(page, PostController.defaultPageSize, PostController.sort))
            : this.postRepository.findAllByIsAllowedTrue(new PageRequest(
                page,
                PostController.defaultPageSize,
                PostController.sort
            )))
            .map(isModer ? PostModel::adminView : PostModel::view)
            .getContent());
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id, HttpServletRequest request) {
        PostModel post = this.postRepository.findOne(id);
        if (post == null) {
            throw new NotFoundException(String.format("post #%d not found", id));
        }

        try {
            this.needModer(request);

            return post.adminView();
        } catch (ForbiddenException e) {/* nop */}

        return post.view();
    }
}
