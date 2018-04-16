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

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class AdminController extends BaseRestController {
    protected final PostRepository postRepository;

    @Autowired
    public AdminController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post, HttpSession session) {
        this.needModer(session);
        UserModel user = this.getUser(session);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException();
        }

        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setModeratedAt(new Date());
        postModel.setUpdatedAt(new Date());
        postModel.setModerator(user);

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    public AdminPostView add(@RequestBody PostView post, HttpSession session) {
        this.needModer(session);
        UserModel user = this.getUser(session);

        PostModel postModel = new PostModel();
        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setCreatedAt(new Date());
        postModel.setModeratedAt(new Date());
        postModel.setModerator(user);
        postModel.setAllowed(true);

        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    public void delete(@PathVariable("id") Long id, HttpSession session) {
        this.needModer(session);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException();
        }

        this.postRepository.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    public AdminPostView approve(@PathVariable("id") Long id, HttpSession session) {
        this.needModer(session);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException();
        }
        postModel.approve(this.getUser(session));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    public AdminPostView dismiss(@PathVariable("id") Long id, HttpSession session) {
        this.needModer(session);

        PostModel postModel = this.postRepository.findOne(id);
        if (postModel == null) {
            throw new NotFoundException();
        }
        postModel.dismiss(this.getUser(session));
        postModel = this.postRepository.save(postModel);

        return postModel.adminView();
    }
}
