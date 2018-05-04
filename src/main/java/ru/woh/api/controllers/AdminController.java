package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.NotFoundException;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;
import ru.woh.api.models.UserModel;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;
import ru.woh.api.views.TagView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AdminController extends BaseRestController {
    protected final PostRepository postRepository;

    @Autowired
    public AdminController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post, HttpServletRequest request) {
        this.needModer(request);
        UserModel user = this.getUser(request);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException(String.format("post #%d not found", id));
        }

        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setModeratedAt(new Date());
        postModel.setUpdatedAt(new Date());
        postModel.setModerator(user);
        postModel.setTags(
            post.getTags()
                .stream()
                .map(TagView::model)
                .collect(Collectors.toSet())
        );

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    public AdminPostView add(@RequestBody PostView post, HttpServletRequest request) {
        this.needModer(request);
        UserModel user = this.getUser(request);

        PostModel postModel = new PostModel();
        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setCreatedAt(new Date());
        postModel.setModeratedAt(new Date());
        postModel.setModerator(user);
        postModel.setIsAllowed(true);
        postModel.setTags(
            post.getTags()
                .stream()
                .map(TagView::model)
                .collect(Collectors.toSet())
        );

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException(String.format("post #%d not found", id));
        }

        this.postRepository.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    public AdminPostView approve(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException(String.format("post #%d not found", id));
        }
        postModel.approve(this.getUser(request));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    public AdminPostView dismiss(@PathVariable("id") Long id, HttpServletRequest request) {
        this.needModer(request);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException(String.format("post #%d not found", id));
        }
        postModel.dismiss(this.getUser(request));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }
}
