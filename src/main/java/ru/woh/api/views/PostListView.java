package ru.woh.api.views;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class PostListView {
    protected Integer count;
    protected List<PostView> posts;

    public PostListView(List<PostView> posts) {
        this.count = posts.size();
        this.posts = posts;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PostView> getPosts() {
        return posts;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }
}
