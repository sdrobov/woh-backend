package ru.woh.api.views.admin;

import java.util.List;

public class TeaserListView {
    protected Long totalCount;
    protected Integer totalPages;
    protected Integer currentPage;
    protected List<TeaserView> teasers;

    public TeaserListView(Long totalCount,
                          Integer totalPages,
                          Integer currentPage,
                          List<TeaserView> teasers) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.teasers = teasers;
    }

    public TeaserListView() {
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

    public List<TeaserView> getTeasers() {
        return teasers;
    }

    public void setTeasers(List<TeaserView> teasers) {
        this.teasers = teasers;
    }
}
