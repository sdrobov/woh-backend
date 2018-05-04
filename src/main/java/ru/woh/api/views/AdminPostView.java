package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.CommentModel;
import ru.woh.api.models.TagModel;
import ru.woh.api.models.UserModel;

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

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments, Set<TagModel> tags) {
        super(id, title, text, source, createdAt, comments, tags);
    }

    public AdminPostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments, Set<TagModel> tags, Date updatedAt, Date moderatedAt, UserModel moderator, Boolean isAllowed) {
        super(id, title, text, source, createdAt, comments, tags);
        this.updatedAt = updatedAt;
        this.moderatedAt = moderatedAt;
        this.moderator = moderator != null ? moderator.view() : null;
        this.isAllowed = isAllowed;
        this.isModerated = moderatedAt != null;
    }
}
