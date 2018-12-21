package ru.woh.api.views.site;

import java.util.List;

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

    public PostListView() {
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public Integer getTotalPages() {
        return this.totalPages;
    }

    public Integer getCurrentPage() {
        return this.currentPage;
    }

    public List<PostView> getPosts() {
        return this.posts;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }
}
