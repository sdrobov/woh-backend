package ru.woh.api.views;

import ru.woh.api.models.CommentModel;
import ru.woh.api.models.UserModel;

import java.util.Date;
import java.util.Set;

public class AdminPostView extends PostView {
    protected Date updatedAt;
    protected Date moderatedAt;
    protected UserView moderator;
    protected Boolean isAllowed;
    protected Boolean isModerated;

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments) {
        super(id, title, text, source, createdAt, comments);
    }

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments, Date updatedAt, Date moderatedAt, UserModel moderator, Boolean isAllowed) {
        super(id, title, text, source, createdAt, comments);
        this.updatedAt = updatedAt;
        this.moderatedAt = moderatedAt;
        this.moderator = moderator != null ? moderator.view() : null;
        this.isAllowed = isAllowed;
        this.isModerated = moderatedAt != null;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getModeratedAt() {
        return moderatedAt;
    }

    public void setModeratedAt(Date moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public UserView getModerator() {
        return moderator;
    }

    public void setModerator(UserView moderator) {
        this.moderator = moderator;
    }

    public Boolean getAllowed() {
        return isAllowed;
    }

    public void setAllowed(Boolean allowed) {
        isAllowed = allowed;
    }

    public Boolean getModerated() {
        return isModerated;
    }

    public void setModerated(Boolean moderated) {
        isModerated = moderated;
    }
}
