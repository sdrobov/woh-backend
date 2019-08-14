package ru.woh.api.requests;

import ru.woh.api.services.ImageStorageService;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AvatarChangeRequest {
    private String avatar;
    private Integer x1;
    private Integer y1;
    private Integer x2;
    private Integer y2;
    private Integer h;
    private Integer w;

    public AvatarChangeRequest() {
    }

    public Boolean isValid() {
        return !this.avatar.isEmpty()
            && this.x1 != null && this.x1 > 0
            && this.y1 != null && this.y1 > 0
            && this.x2 != null && this.x2 > 0
            && this.y2 != null && this.y2 > 0
            && this.h != null && this.h > 0
            && this.w != null && this.w > 0;
    }

    public BufferedImage getBufferedImage() {
        BufferedImage originalAvatar = ImageStorageService.fromBase64(this.avatar);
        if (originalAvatar == null) {
            return null;
        }

        int newWidth, newHeight;
        newWidth = newHeight = Math.max(this.w, this.h);

        BufferedImage crop1 = originalAvatar.getSubimage(this.x1, this.y1, newWidth, newHeight);
        BufferedImage crop2 = null;
        if (crop1.getWidth() > 1000 || crop1.getHeight() > 1000) {
            crop2 = new BufferedImage(1000, 1000, crop1.getType());
            Graphics2D graphics2D = crop2.createGraphics();

            if (crop2.getType() == BufferedImage.TYPE_INT_ARGB) {
                graphics2D.setComposite(AlphaComposite.Src);
            }

            graphics2D.drawImage(crop1, 0, 0, 1000, 1000, null);
            graphics2D.dispose();
        }

        return crop2 != null ? crop2 : crop1;
    }

    public Integer getX1() {
        return this.x1;
    }

    public Integer getY1() {
        return this.y1;
    }

    public Integer getX2() {
        return this.x2;
    }

    public Integer getY2() {
        return this.y2;
    }

    public Integer getH() {
        return this.h;
    }

    public Integer getW() {
        return this.w;
    }

    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    public void setY1(Integer y1) {
        this.y1 = y1;
    }

    public void setX2(Integer x2) {
        this.x2 = x2;
    }

    public void setY2(Integer y2) {
        this.y2 = y2;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
