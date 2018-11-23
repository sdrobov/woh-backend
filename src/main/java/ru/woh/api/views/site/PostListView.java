package ru.woh.api.views.site;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostListView {
    protected Long totalCount;
    protected Integer totalPages;
    protected Integer currentPage;
    protected List<PostView> posts;

    public PostListView(Long totalCount, Integer totalPages, Integer currentPage, List<PostView> posts) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.posts = posts;
    }
}
