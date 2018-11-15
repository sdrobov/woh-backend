package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.woh.api.models.Source;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostPreviewView {
    private Long id;
    private String title;
    private String text;
    private String announce;
    private Date createdAt;
    private SourceView source;

    public PostPreviewView(
        Long id,
        String title,
        String text,
        String announce,
        Date createdAt,
        Source source
    ) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.announce = announce;
        this.createdAt = createdAt;
        this.source = source.view();
    }
}
