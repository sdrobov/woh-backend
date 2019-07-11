package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.Teaser;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.models.repositories.TeaserRepository;
import ru.woh.api.views.admin.AdminPostView;
import ru.woh.api.views.admin.TeaserView;
import ru.woh.api.views.site.PostListView;
import ru.woh.api.views.site.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminTeaserController {
    private final TeaserRepository teaserRepository;
    private final PostRepository postRepository;

    @Autowired
    public AdminTeaserController(
        TeaserRepository teaserRepository,
        PostRepository postRepository
    ) {
        this.teaserRepository = teaserRepository;
        this.postRepository = postRepository;
    }

    @PostMapping({"/teasers", "/teasers/"})
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public PostView add(TeaserView teaserView) {
        Post post = this.postRepository.findById(teaserView.getPost().getId())
            .orElseThrow(() -> new HttpClientErrorException(
                HttpStatus.NOT_FOUND, String.format("Post #%d not found", teaserView.getPost().getId())));

        Teaser teaser = new Teaser();
        teaser.setFrom(teaserView.getFrom());
        teaser.setTo(teaserView.getTo());
        teaser.setIsTeaser(teaserView.getTeaser() ? (short) 1 : 0);
        teaser.setPost(post);
        teaser.autoCreatePk();

        return this.teaserRepository.save(teaser).getPost().adminView();
    }

    @PostMapping({"/teasers/delete", "/teasers/delete/"})
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity delete(TeaserView teaserView) {
        Teaser.TeaserPK teaserPK = new Teaser.TeaserPK(
            teaserView.getFrom(),
            teaserView.getTo(),
            teaserView.getPost().getId(),
            teaserView.getTeaser() ? (short) 1 : 0
        );

        Teaser teaser = this.teaserRepository.findById(teaserPK)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.teaserRepository.delete(teaser);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping({"/teasers/all", "/teasers/all/"})
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public PostListView all(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        var result = this.teaserRepository.findAll(PageRequest.of(page, 20));

        var postListView = new PostListView();
        postListView.setCurrentPage(page);
        postListView.setPosts(
            result.get()
                .map(Teaser::getPost)
                .map(Post::adminView)
                .collect(Collectors.toList())
        );
        postListView.setTotalCount(result.getTotalElements());
        postListView.setTotalPages(result.getTotalPages());

        return postListView;
    }

    @GetMapping({"/teasers/{id:[0-9]*}", "/teasers/{id:[0-9]*}/"})
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public List<AdminPostView> byPostId(@PathVariable("id") Long id) {
        var post = this.postRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND,
                String.format("Post #%d not found", id)));

        return this.teaserRepository.findAllByPost(post)
            .stream()
            .map(Teaser::getPost)
            .map(Post::adminView)
            .collect(Collectors.toList());
    }

    @GetMapping({"/teasers/future", "/teasers/future/"})
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public List<AdminPostView> future() {
        return this.teaserRepository.findFromFuture()
            .stream()
            .map(Teaser::getPost)
            .map(Post::adminView)
            .collect(Collectors.toList());
    }
}
