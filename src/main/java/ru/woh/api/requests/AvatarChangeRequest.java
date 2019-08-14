package ru.woh.api.requests;

import ru.woh.api.services.ImageStorageService;

import java.awt.image.BufferedImage;

public class AvatarChangeRequest {
    private String avatar;

    public AvatarChangeRequest() {
    }

    public Boolean isValid() {
        return !this.avatar.isEmpty();
    }

    public BufferedImage getBufferedImage() {
        return ImageStorageService.fromBase64(this.avatar);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
