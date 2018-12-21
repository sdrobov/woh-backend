package ru.woh.api.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.Teaser;
import ru.woh.api.models.repositories.TeaserRepository;
import ru.woh.api.views.site.PostView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TeaserController {
    private final TeaserRepository teaserRepository;

    @Autowired
    public TeaserController(
        TeaserRepository teaserRepository
    ) {
        this.teaserRepository = teaserRepository;
    }

    @GetMapping({ "/teasers", "/teasers/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<PostView> teasers() {
        return this.teaserRepository
            .findTodayTeasers()
            .stream()
            .map(Teaser::getPost)
            .map(Post::view)
            .collect(Collectors.toList());
    }

    @GetMapping({ "/featured", "/featuerd/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<PostView> featured() {
        return this.teaserRepository
            .findTodayFeatured()
            .stream()
            .map(Teaser::getPost)
            .map(Post::view)
            .collect(Collectors.toList());
    }
}
