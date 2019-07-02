package ru.woh.api.views.admin;

import ru.woh.api.models.Category;
import ru.woh.api.models.Source;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;
import ru.woh.api.views.site.PostView;
import ru.woh.api.views.site.UserView;

import java.util.Date;
import java.util.Set;

public class AdminPostView extends PostView {
    private Date updatedAt;
    private Date moderatedAt;
    private UserView moderator;
    private Boolean isAllowed;
    private Boolean isModerated;

    public AdminPostView(
        Long id,
        String title,
        String text,
        Source source,
        Date createdAt,
        Date publishedAt,
        Set<Tag> tags,
        Set<Category> categories, String announce,
        User proposedBy,
        String teaserImage,
        String featuredImage,
        String nearestImage,
        Boolean canBeNearest,
        Date updatedAt,
        Date moderatedAt,
        User moderator,
        Boolean isAllowed
    ) {
        super(
            id,
            title,
            text,
            source,
            createdAt,
            publishedAt,
            tags,
            categories, announce,
            proposedBy,
            teaserImage,
            featuredImage,
            nearestImage,
            canBeNearest
        );
        this.updatedAt = updatedAt;
        this.moderatedAt = moderatedAt;
        this.moderator = moderator != null ? moderator.view() : null;
        this.isAllowed = isAllowed;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public Date getModeratedAt() {
        return this.moderatedAt;
    }

    public UserView getModerator() {
        return this.moderator;
    }

    public Boolean getIsAllowed() {
        return this.isAllowed;
    }

    public Boolean getIsModerated() {
        return this.isModerated;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setModeratedAt(Date moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public void setModerator(UserView moderator) {
        this.moderator = moderator;
    }

    public void setIsAllowed(Boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public void setIsModerated(Boolean isModerated) {
        this.isModerated = isModerated;
    }
}
