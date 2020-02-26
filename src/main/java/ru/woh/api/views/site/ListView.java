package ru.woh.api.views.site;

import java.util.List;

public class ListView<T> {
    protected Long totalCount;
    protected Integer totalPages;
    protected Integer currentPage;
    protected List<T> items;

    public ListView() {
    }

    public ListView(Long totalCount, Integer totalPages, Integer currentPage, List<T> items) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.items = items;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
