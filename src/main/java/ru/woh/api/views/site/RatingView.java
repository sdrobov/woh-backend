package ru.woh.api.views.site;

public class RatingView {
    private Long count;
    private Boolean like;
    private Boolean dislike;

    public RatingView() {
    }

    public Long getCount() {
        return this.count;
    }

    public Boolean getLike() {
        return this.like;
    }

    public Boolean getDislike() {
        return this.dislike;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public void setDislike(Boolean dislike) {
        this.dislike = dislike;
    }
}
