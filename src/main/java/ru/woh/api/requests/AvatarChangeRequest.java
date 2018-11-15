package ru.woh.api.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@NoArgsConstructor
@Getter
@Setter
public class AvatarChangeRequest {
    protected MultipartFile file;
    protected Integer x1;
    protected Integer y1;
    protected Integer x2;
    protected Integer y2;
    protected Integer h;
    protected Integer w;

    public Boolean isValid() {
        return !this.file.isEmpty()
            && this.x1 != null
            && this.y1 != null
            && this.x2 != null
            && this.y2 != null
            && this.h != null
            && this.w != null;
    }

    public BufferedImage getBufferedImage() {
        try {
            BufferedImage originalAvatar = ImageIO.read(new ByteArrayInputStream(this.file.getBytes()));
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
