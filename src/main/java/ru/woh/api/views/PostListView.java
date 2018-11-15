package ru.woh.api.views;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostListView {
    protected Long totalCount;
    protected Long totalPages;
    protected Long currentPage;
    protected List<PostView> posts;

    public PostListView(Long totalCount, Long totalPages, Long currentPage, List<PostView> posts) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.posts = posts;
    }
}
