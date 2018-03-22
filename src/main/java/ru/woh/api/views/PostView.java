package ru.woh.api.views;

import lombok.NoArgsConstructor;
import ru.woh.api.models.CommentModel;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PostView {
    protected Long id;
    protected String title;
    protected String text;
    protected String source;
    protected Date createdAt;
    protected List<CommentView> comments;

    public PostView(Long id, String title, String text, String source, Date createdAt, Set<CommentModel> comments) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentView> getComments() {
        return comments;
    }

    public void setComments(List<CommentView> comments) {
        this.comments = comments;
    }
}
