package ru.woh.api.controllers;

import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.services.ImageStorageService;
import ru.woh.api.models.Role;

@RestController
public class ImageController {
    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/image/{id:.*}")
    @RolesAllowed({ Role.ROLE_ANONYMOUS, Role.ROLE_USER, Role.ROLE_MODER, Role.ROLE_ADMIN })
    public ResponseEntity<byte[]> image(@PathVariable("id") String id) {
        return this.imageStorageService.getImage(id);
    }
}
