package ru.woh.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

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

        var dateFormat = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        var expires = Date.from(new Date().toInstant().plus(Period.ofDays(365 * 10)));
        var lastModified = gridfsFile.getUploadDate() != null ? gridfsFile.getUploadDate() : new Date();

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, contentType)
            .header(HttpHeaders.EXPIRES, dateFormat.format(expires))
            .header(HttpHeaders.LAST_MODIFIED, dateFormat.format(lastModified))
            .body(image);
    }

    public void dropImage(String id) {
        var image = this.gridFsService.findById(id);

        if (image != null) {
            this.gridFsService.delete(image);
        }
    }

    static BufferedImage fromBase64(String base64Encoded) {
        var encoded = base64Encoded.indexOf(',') > 0 ? base64Encoded.split(",")[1] : base64Encoded;

        var bytes = DatatypeConverter.parseBase64Binary(encoded);
        var bais = new ByteArrayInputStream(bytes);

        try {
            var image = ImageIO.read(bais);
            bais.close();

            return image;
        } catch (IOException e) {
            return null;
        }
    }
}
