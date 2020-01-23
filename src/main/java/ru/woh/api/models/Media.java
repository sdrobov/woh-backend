package ru.woh.api.models;

import org.hibernate.annotations.Type;
import ru.woh.api.views.site.ImageView;
import ru.woh.api.views.site.MediaView;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Media")
@Table(name = "media")
public class Media implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    private String title;

    @Type(type = "text")
    private String url;

    @Column(name = "embed_code")
    @Type(type = "text")
    private String embedCode;

    private String thumbnail;

    @ManyToMany
    @JoinTable(name = "comment_ref_media",
        joinColumns = {@JoinColumn(name = "media_id")},
        inverseJoinColumns = {@JoinColumn(name = "comment_id")})
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "post_ref_media",
        joinColumns = {@JoinColumn(name = "media_id")},
        inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Comment> posts = new HashSet<>();

    public Media() {
    }

    public MediaView view() {
        var image = new ImageView();
        image.setUrl(this.thumbnail);

        return new MediaView(this.id, this.title, this.url, this.embedCode, image);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Comment> getPosts() {
        return posts;
    }

    public void setPosts(Set<Comment> posts) {
        this.posts = posts;
    }
}
