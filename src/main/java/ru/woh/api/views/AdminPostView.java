package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Comment;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class AdminPostView extends PostView {
    protected Date updatedAt;
    protected Date moderatedAt;
    protected UserView moderator;
    protected Boolean isAllowed;
    protected Boolean isModerated;

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<Comment> comments, Set<Tag> tags, String announce) {
        super(id, title, text, source, createdAt, comments, tags, announce);
    }

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<Comment> comments, Set<Tag> tags, String announce, Date updatedAt, Date moderatedAt, User moderator, Boolean isAllowed) {
        super(id, title, text, source, createdAt, comments, tags, announce);
        this.updatedAt = updatedAt;
        this.moderatedAt = moderatedAt;
        this.moderator = moderator != null ? moderator.view() : null;
        this.isAllowed = isAllowed;
        this.isModerated = moderatedAt != null;
    }
}
