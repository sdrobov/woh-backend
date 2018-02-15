package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.CommentModel;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor @Getter @Setter
public class PostView {
    private long id;
    private String title;
    private String text;
    private String source;
    private Date createdAt;
    private List<CommentView> comments;

    public PostView(long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments) {
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
    }
}
