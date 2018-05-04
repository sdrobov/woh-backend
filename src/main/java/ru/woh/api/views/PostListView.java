package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostListView {
    protected Integer count;
    protected List<PostView> posts;

    public PostListView(List<PostView> posts) {
        this.count = posts.size();
        this.posts = posts;
    }
}
