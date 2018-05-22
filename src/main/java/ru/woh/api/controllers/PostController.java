package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.ForbiddenException;
import ru.woh.api.NotFoundException;
import ru.woh.api.PostService;
import ru.woh.api.models.PostModel;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
public class PostController extends BaseRestController {
    private final PostService postService;
    private static final int defaultPageSize = 100;

    @Autowired public PostController(PostService postService) {
        this.postService = postService;
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

        return new PostListView(this.postService.list(page, PostController.defaultPageSize, isModer)
            .stream()
            .map(isModer ? PostModel::adminView : PostModel::view)
            .collect(Collectors.toList()));
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id, HttpServletRequest request) {
        PostModel post = this.postService.one(id);
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
