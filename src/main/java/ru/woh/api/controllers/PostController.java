package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.services.PostService;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

@RestController
public class PostController {
    private final PostService postService;
    private static final int defaultPageSize = 100;

    @Autowired public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return this.postService.listView(page, PostController.defaultPageSize);
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id) {
        return this.postService.view(id);
    }
}
