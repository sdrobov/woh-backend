package ru.woh.api.views.site;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Tag;
import ru.woh.api.models.User;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class PostView {
    protected Long id;
    protected String title;
    protected String text;
    protected String announce;
    protected String source;
    protected Date createdAt;
    protected Date publishedAt;
    protected List<CommentView> comments;
    protected List<String> tags;
    protected RatingView rating;
    protected Long totalComments;
    protected UserView proposedBy;

    public PostView(
        Long id,
        String title,
        String text,
        String source,
        Date createdAt,
        Date publishedAt,
        Set<Tag> tags,
        String announce,
        User proposedBy
    ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
        this.tags = tags != null ? tags.stream().map(Tag::getName).sorted().collect(Collectors.toList()) : null;
        this.announce = announce;
        this.proposedBy = proposedBy != null ? proposedBy.view() : null;
    }
}
