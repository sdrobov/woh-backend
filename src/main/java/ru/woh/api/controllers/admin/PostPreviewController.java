package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.PostPreview;
import ru.woh.api.models.Role;
import ru.woh.api.models.repositories.PostPreviewRepository;
import ru.woh.api.models.repositories.SourceRepository;
import ru.woh.api.views.admin.PostPreviewView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostPreviewController {
    private final PostPreviewRepository postPreviewRepository;
    private final SourceRepository sourceRepository;

    @Autowired
    public PostPreviewController(
        PostPreviewRepository postPreviewRepository,
        SourceRepository sourceRepository
    ) {
        this.postPreviewRepository = postPreviewRepository;
        this.sourceRepository = sourceRepository;
    }

    @GetMapping("/post-preview/")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public List<PostPreviewView> list() {
        return this.postPreviewRepository.findAll()
            .stream()
            .map(PostPreview::view)
            .collect(Collectors.toList());
    }

    @GetMapping("/post-preview/{id:[0-9]+}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public PostPreviewView byId(@PathVariable("id") Long id) {
        return this.postPreviewRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
                "post preview %d not found",
                id
            )))
            .view();
    }

    @GetMapping("/post-preview/by-source/{id:[0-9]+}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public List<PostPreviewView> bySource(@PathVariable("id") Long id) {
        var source = this.sourceRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, String.format(
                "source %d not found",
                id
            )));

        return this.postPreviewRepository.findAllBySource(source)
            .stream()
            .map(PostPreview::view)
            .collect(Collectors.toList());
    }
}
