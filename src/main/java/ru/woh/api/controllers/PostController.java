package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.ForbiddenException;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;
import ru.woh.api.views.PostListView;
import ru.woh.api.views.PostView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController extends BaseRestController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public PostListView list(@RequestParam(value = "page", defaultValue = "0") Integer page, HttpServletRequest request) {
        Boolean isModer = false;
        try {
            this.needModer(request);
            isModer = true;
        } catch (ForbiddenException e) {/* nop */}
        return new PostListView(this.postRepository.findAllApproved(new PageRequest(page, 100))
                .map(isModer ? PostModel::adminView : PostModel::view)
                .getContent());
    }

    @GetMapping("/{id:[0-9]*}")
    public PostView one(@PathVariable("id") Long id, HttpServletRequest request) {
        try {
            this.needModer(request);

            return this.postRepository.findOne(id).adminView();
        } catch (ForbiddenException e) {/* nop */}

        return this.postRepository.findOne(id).view();
    }
}
