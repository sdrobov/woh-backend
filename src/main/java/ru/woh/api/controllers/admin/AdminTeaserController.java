package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import ru.woh.api.models.Post;
import ru.woh.api.models.Role;
import ru.woh.api.models.Teaser;
import ru.woh.api.models.repositories.PostRepository;
import ru.woh.api.models.repositories.TeaserRepository;
import ru.woh.api.views.admin.TeaserView;

import javax.annotation.security.RolesAllowed;

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

    @PostMapping({ "/teasers", "/teasers/" })
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public TeaserView add(TeaserView teaserView) {
        Post post = this.postRepository.findById(teaserView.getPost().getId())
            .orElseThrow(() -> new HttpClientErrorException(
                HttpStatus.NOT_FOUND, String.format("Post #%d not found", teaserView.getPost().getId())));

        Teaser teaser = new Teaser();
        teaser.setFrom(teaserView.getFrom());
        teaser.setTo(teaserView.getTo());
        teaser.setIsTeaser(teaserView.getTeaser() ? (short)1 : 0);
        teaser.setPost(post);
        teaser.autoCreatePk();

        return this.teaserRepository.save(teaser).adminView();
    }

    @PostMapping({ "/teasers/delete", "/teasers/delete/" })
    @RolesAllowed({ Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ResponseEntity delete(TeaserView teaserView) {
        Teaser.TeaserPK teaserPK = new Teaser.TeaserPK(
            teaserView.getFrom(),
            teaserView.getTo(),
            teaserView.getPost().getId(),
            teaserView.getTeaser() ? (short)1 : 0
        );

        Teaser teaser = this.teaserRepository.findById(teaserPK)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        this.teaserRepository.delete(teaser);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
