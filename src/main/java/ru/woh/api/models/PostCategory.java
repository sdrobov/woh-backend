package ru.woh.api.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "categories_ref_posts")
public class PostCategory implements Serializable {
    @Embeddable
    public static class PostCategoryPk implements Serializable {
        @Column(name = "post_id", nullable = false)
        private Long postId;

        @Column(name = "category_id", nullable = false)
        private Long categoryId;

        public PostCategoryPk() {
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        @Override public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PostCategoryPk)) {
                return false;
            }
            PostCategoryPk that = (PostCategoryPk) o;
            return postId.equals(that.postId) &&
                categoryId.equals(that.categoryId);
        }

        @Override public int hashCode() {
            return Objects.hash(postId, categoryId);
        }
    }

    @EmbeddedId
    @Column(unique = true)
    private PostCategoryPk postCategoryPk;

    @Column(name = "post_id", insertable = false, updatable = false)
    private Long postId;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    public PostCategory() {
    }

    public PostCategoryPk getPostCategoryPk() {
        return postCategoryPk;
    }

    public void setPostCategoryPk(PostCategoryPk postCategoryPk) {
        this.postCategoryPk = postCategoryPk;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostCategory)) {
            return false;
        }
        PostCategory that = (PostCategory) o;
        return postId.equals(that.postId) &&
            categoryId.equals(that.categoryId);
    }

    @Override public int hashCode() {
        return Objects.hash(postId, categoryId);
    }
}
