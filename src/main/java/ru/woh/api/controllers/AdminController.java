package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.SourceRepository;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.ParserService;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;
import ru.woh.api.views.SourceView;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final SourceRepository sourceRepository;
    private final ParserService parserService;

    @Autowired
    public AdminController(
        UserService userService,
        PostService postService,
        TagRepository tagRepository,
        SourceRepository sourceRepository,
        ParserService parserService
    ) {
        this.userService = userService;
        this.postService = postService;
        this.tagRepository = tagRepository;
        this.sourceRepository = sourceRepository;
        this.parserService = parserService;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView savePost(@PathVariable("id") Long id, @RequestBody PostView post) {
        User user = this.userService.getCurrenttUser();

        Post postModel = this.postService.one(id);

        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setModeratedAt(new Date());
        postModel.setUpdatedAt(new Date());
        postModel.setModerator(user);
        postModel.setAnnounce(post.getAnnounce());
        if (post.getTags() != null) {
            postModel.setTags(
                post.getTags()
                    .stream()
                    .map(String::trim)
                    .distinct()
                    .map(tagName -> this.tagRepository.findFirstByName(tagName).orElseGet(() -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }))
                    .collect(Collectors.toSet())
            );
        }

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/add")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView addPost(@RequestBody PostView post) {
        User user = this.userService.getCurrenttUser();

        Post postModel = new Post();
        postModel.setTitle(post.getTitle());
        postModel.setText(post.getText());
        postModel.setSource(post.getSource());
        postModel.setCreatedAt(new Date());
        postModel.setModeratedAt(new Date());
        postModel.setModerator(user);
        postModel.setIsAllowed(true);
        postModel.setAnnounce(post.getAnnounce());
        if (post.getTags() != null) {
            postModel.setTags(
                post.getTags()
                    .stream()
                    .map(String::trim)
                    .distinct()
                    .map(tagName -> this.tagRepository.findFirstByName(tagName).orElseGet(() -> {
                        Tag tag = new Tag();
                        tag.setName(tagName);
                        return this.tagRepository.save(tag);
                    }))
                    .collect(Collectors.toSet())
            );
        }

        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/delete")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public void deletePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView approvePost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView dismissPost(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @GetMapping("/source/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public List<SourceView> listSource() {
        return this.sourceRepository.findAll()
            .getContent()
            .stream()
            .map(Source::view)
            .collect(Collectors.toList());
    }

    @GetMapping("/source/run/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public ResponseEntity runSource(@PathVariable("id") Long id) {
        Source source = this.sourceRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "source #%d not found",
            id
        )));

        if (this.parserService.parseSource(source)) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/source/add/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public SourceView addSource(@RequestBody SourceView sourceView) {
        Source source = sourceView.model();
        source.setId(null);

        return this.sourceRepository.save(source).view();
    }

    @PostMapping("/source/edit/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public SourceView editSource(@RequestBody SourceView sourceView) {
        if (sourceView.getId() == null || !this.sourceRepository.existsById(sourceView.getId())) {
            throw new NotFoundException("source not found");
        }

        return this.sourceRepository.save(sourceView.model()).view();
    }

    @PostMapping("/source/delete/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public ResponseEntity deleteSource(@PathVariable Long id) {
        if (!this.sourceRepository.existsById(id)) {
            throw new NotFoundException("source not found");
        }

        this.sourceRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
