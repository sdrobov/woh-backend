package ru.woh.api.views.admin;

import ru.woh.api.models.Tag;
import ru.woh.api.models.User;
import ru.woh.api.views.site.PostView;
import ru.woh.api.views.site.UserView;

import java.util.Date;
import java.util.Set;

public class AdminPostView extends PostView {
    protected Date updatedAt;
    protected Date moderatedAt;
    protected UserView moderator;
    protected Boolean isAllowed;
    protected Boolean isModerated;

    public AdminPostView(
        Long id,
        String title,
        String text,
        String source,
        Date createdAt,
        Date publishedAt,
        Set<Tag> tags,
        String announce,
        User proposedBy,
        Date updatedAt,
        Date moderatedAt,
        User moderator,
        Boolean isAllowed
    ) {
        super(id, title, text, source, createdAt, publishedAt, tags, announce, proposedBy);
        this.updatedAt = updatedAt;
        this.moderatedAt = moderatedAt;
        this.moderator = moderator != null ? moderator.view() : null;
        this.isAllowed = isAllowed;
        this.isModerated = moderatedAt != null;
    }

    public AdminPostView() {
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
