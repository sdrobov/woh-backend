package ru.woh.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.models.*;
import ru.woh.api.models.repositories.SourceRepository;
import ru.woh.api.models.repositories.TagRepository;
import ru.woh.api.services.PostService;
import ru.woh.api.services.UserService;
import ru.woh.api.views.AdminPostView;
import ru.woh.api.views.PostView;
import ru.woh.api.views.SourceView;

import javax.annotation.security.RolesAllowed;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    private final PostService postService;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final SourceRepository sourceRepository;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CrawlerParseResponse {
        private String status;
        private String error;

        static CrawlerParseResponse fromJsonString(String json) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(json, CrawlerParseResponse.class);
            } catch (IOException e) {
                CrawlerParseResponse response = new CrawlerParseResponse();
                response.setError("json parse error");

                return response;
            }
        }
    }

    @Autowired
    public AdminController(
        UserService userService,
        PostService postService,
        TagRepository tagRepository,
        SourceRepository sourceRepository
    ) {
        this.userService = userService;
        this.postService = postService;
        this.tagRepository = tagRepository;
        this.sourceRepository = sourceRepository;
    }

    @PostMapping("/{id:[0-9]*}")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView save(@PathVariable("id") Long id, @RequestBody PostView post) {
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
    public AdminPostView add(@RequestBody PostView post) {
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
    public void delete(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        this.postService.delete(postModel);
    }

    @PostMapping("/{id:[0-9]*}/approve")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView approve(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.approve(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @PostMapping("/{id:[0-9]*}/dismiss")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public AdminPostView dismiss(@PathVariable("id") Long id) {
        Post postModel = this.postService.one(id);

        postModel.dismiss(this.userService.getCurrenttUser());
        postModel = this.postService.save(postModel);

        return postModel.adminView();
    }

    @GetMapping("/sources/")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public List<SourceView> sourcesList() {
        return this.sourceRepository.findAll()
            .getContent()
            .stream()
            .map(Source::view)
            .collect(Collectors.toList());
    }

    @GetMapping("/sources/run/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_MODER, Role.ROLE_ADMIN})
    public ResponseEntity<CrawlerParseResponse> runSource(@PathVariable("id") Long id) throws IOException {
        if (!this.sourceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        URL url = new URL(String.format("http://localhost:3000/?sourceId=%d", id));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int resCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        CrawlerParseResponse crawlerParseResponse = CrawlerParseResponse.fromJsonString(response.toString());

        return ResponseEntity.status(resCode).body(crawlerParseResponse);
    }
}
