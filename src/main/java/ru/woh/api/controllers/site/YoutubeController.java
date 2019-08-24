package ru.woh.api.controllers.site;

import com.google.api.services.youtube.model.VideoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.models.Role;
import ru.woh.api.services.YoutubeService;

import javax.annotation.security.RolesAllowed;

@RestController
public class YoutubeController {
    private final YoutubeService youtubeService;

    @Autowired
    public YoutubeController(YoutubeService youtubeService) {
        this.youtubeService = youtubeService;
    }

    @GetMapping({ "/youtube/{id:.*}", "/youtube/{id:.*}/" })
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public VideoListResponse byId(@PathVariable String id) {
        return this.youtubeService.getListById(id);
    }
}
