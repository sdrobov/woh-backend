package ru.woh.api.models;

import ru.woh.api.views.admin.TeaserView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Teaser")
@Table(name = "teaser")
public class Teaser implements Serializable {
    @Embeddable
    public static class TeaserPK implements Serializable {
        private Date from;
        private Date to;

        @Column(name = "post_id", nullable = false)
        private Long postId;

        @Column(name = "is_teaser", nullable = false)
        private Short isTeaser;

        public TeaserPK(Date from, Date to, Long postId, Short isTeaser) {
            this.from = from;
            this.to = to;
            this.postId = postId;
            this.isTeaser = isTeaser;
        }

        public Date getFrom() {
            return from;
        }

        public void setFrom(Date from) {
            this.from = from;
        }

        public Date getTo() {
            return to;
        }

        public void setTo(Date to) {
            this.to = to;
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }

        public Short getIsTeaser() {
            return isTeaser;
        }

        public void setIsTeaser(Short isTeaser) {
            this.isTeaser = isTeaser;
        }
    }

    @EmbeddedId
    @Column(unique = true)
    private TeaserPK pk;

    @Column(insertable = false, updatable = false)
    private Date from;

    @Column(insertable = false, updatable = false)
    private Date to;

    @ManyToOne
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @Column(name = "is_teaser", insertable = false, updatable = false)
    private Short isTeaser;

    public TeaserView adminView() {
        return new TeaserView(this.from, this.to, this.post, this.isTeaser == 1);
    }

    public void autoCreatePk() {
        this.pk = new TeaserPK(this.from, this.to, this.post.getId(), this.isTeaser);
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Short getIsTeaser() {
        return isTeaser;
    }

    public void setIsTeaser(Short isTeaser) {
        this.isTeaser = isTeaser;
    }

    public TeaserPK getPk() {
        return pk;
    }

    public void setPk(TeaserPK pk) {
        this.pk = pk;
    }
}
