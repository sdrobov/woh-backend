package ru.woh.api.controllers.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.Category;
import ru.woh.api.models.Role;
import ru.woh.api.models.repositories.CategoryRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.views.site.PostListView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryController {
    private final PostService postService;
    private final CategoryRepository categoryRepository;
    private static final int defaultPageSize = 5;

    @Autowired
    public CategoryController(
        PostService postService,
        CategoryRepository categoryRepository
    ) {
        this.postService = postService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping({ "/by-category/{category:.*}", "/by-category/{category:.*}/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public PostListView byCategory(
        @PathVariable("category") String category,
        @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        return this.postService.byCategory(page, CategoryController.defaultPageSize, category);
    }

    @GetMapping({ "/categories", "/categories/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public List<String> list() {
        return this.categoryRepository.findAll()
            .stream()
            .map(Category::getName)
            .map(String::trim)
            .distinct()
            .collect(Collectors.toList());
    }
}
