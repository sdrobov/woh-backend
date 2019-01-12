package ru.woh.api.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.Role;
import ru.woh.api.models.Tag;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.views.site.PostListView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TagController {
    private final PostService postService;
    private final TagRepository tagRepository;
    private static final int defaultPageSize = 5;

    @Autowired
    public TagController(PostService postService, TagRepository tagRepository) {
        this.postService = postService;
        this.tagRepository = tagRepository;
    }

    @GetMapping({ "/by-tag/{tag:.*}", "/by-tag/{tag:.*}/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView byTag(
        @PathVariable("tag") String tag,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return this.postService.byTag(page, TagController.defaultPageSize, tag);
    }

    @GetMapping({ "/tags", "/tags/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<String> list() {
        return this.tagRepository.findAll()
            .stream()
            .map(Tag::getName)
            .map(String::trim)
            .map(String::toLowerCase)
            .distinct()
            .collect(Collectors.toList());
    }
}
