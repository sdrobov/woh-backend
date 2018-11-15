package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

@Service
public class ImageStorageService {
    private final GridFsService gridFsService;

    @Autowired
    public ImageStorageService(GridFsService gridFsService) {
        this.gridFsService = gridFsService;
    }

    public String storeBufferedImage(
        BufferedImage bufferedImage,
        String name,
        String contentType,
        HashMap<String, String> meta
    ) {
        if (bufferedImage == null || name == null || contentType == null || meta == null) {
            return null;
        }

        var byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);
        } catch (IOException e) {
            return null;
        }

        var inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        return this.gridFsService.store(
            inputStream,
            name,
            contentType,
            meta
        );
    }

    public ResponseEntity<byte[]> getImage(String id) {
        var gridfsFile = this.gridFsService.findById(id);
        if (gridfsFile == null) {
            return ResponseEntity.notFound().build();
        }

        var image = this.gridFsService.getFile(gridfsFile);
        var contentType = gridfsFile.getMetadata() != null
            ? gridfsFile.getMetadata().get(HttpHeaders.CONTENT_TYPE, "octet/stream")
            : "octet/stream";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, contentType).body(image);
    }

    public void dropImage(String id) {
        var image = this.gridFsService.findById(id);
        this.gridFsService.delete(image);
    }
}
