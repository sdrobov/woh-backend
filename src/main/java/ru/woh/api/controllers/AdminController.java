package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.ForbiddenException;
import ru.woh.api.NotFoundException;
import ru.woh.api.UserService;
import ru.woh.api.models.PostModel;
import ru.woh.api.models.PostRepository;
import ru.woh.api.models.UserModel;
import ru.woh.api.views.PostView;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class AdminController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/{id:[0-9]*}")
    public PostView save(@PathVariable("id") Long id, PostView post, HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null || !user.isModer()) {
            throw new ForbiddenException();
        }

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

        return postModel.view();
    }

    @PostMapping("/add")
    public PostView add(PostView post, HttpSession session) {
        UserModel user = this.userService.getUser(session);
        if (user == null || !user.isModer()) {
            throw new ForbiddenException();
        }

        PostModel postModel = new PostModel();
        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setCreatedAt(new Date());
        postModel.setModeratedAt(new Date());
        postModel.setModerator(user);
        postModel.setModerated(true);
        postModel.setAllowed(true);

        postModel = this.postRepository.save(postModel);

        return postModel.view();
    }
}
