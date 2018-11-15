package ru.woh.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.woh.api.services.ImageStorageService;

@RestController
public class ImageController {
    private final ImageStorageService imageStorageService;

    @Autowired
    public ImageController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @GetMapping("/image/{id:.*}")
    public ResponseEntity<byte[]> image(@PathVariable("id") String id) {
        return this.imageStorageService.getImage(id);
    }
}
