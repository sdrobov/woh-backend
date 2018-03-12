package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

@RestController
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return new PostListView(this.postRepository.findAllApproved(new PageRequest(page, 100))
                .map(PostModel::view)
                .getContent());
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id) {
        return this.postRepository.findOne(id).view();
    }
}
