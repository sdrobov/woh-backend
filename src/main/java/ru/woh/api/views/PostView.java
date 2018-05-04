package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.CommentModel;
import ru.woh.api.models.TagModel;

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
    protected String source;
    protected Date createdAt;
    protected List<CommentView> comments;
    protected List<TagView> tags;

    public PostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments, Set<TagModel> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
        this.comments = comments
            .stream()
            .limit(2)
            .map(CommentModel::view)
            .collect(Collectors.toList());
        this.tags = tags
            .stream()
            .map(TagModel::view)
            .collect(Collectors.toList());
    }
}
