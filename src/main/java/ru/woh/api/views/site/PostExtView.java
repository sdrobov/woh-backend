package ru.woh.api.views.site;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostExtView {
    protected PostView post;
    protected List<PostView> prev;
    protected List<PostView> next;

    public PostExtView(PostView post, List<PostView> prev, List<PostView> next) {
        this.post = post;
        this.prev = prev;
        this.next = next;
    }
}
