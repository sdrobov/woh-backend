package ru.woh.api.models;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import ru.woh.api.views.admin.SourceView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "Source")
@Table(name = "source")
public class Source implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String url;

    @Type(type = "text")
    private String settings;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "last_post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPostDate;

    @Column(name = "is_locked")
    private Integer isLocked;

    @Column(name = "last_success_at")
    private Date lastSuccessAt;

    @Column(name = "last_error_at")
    private Date lastErrorAt;

    @Column(name = "last_success_count")
    private Integer lastSuccessCount;

    @Column(name = "last_errors_count")
    private Integer lastErrorsCount;

    @Column(name = "is_enabled")
    private Integer isEnabled;

    @OneToMany(mappedBy = "sourceSite", fetch = FetchType.LAZY)
    private Set<Post> posts;

    public Source() {
    }

    public SourceView view() {
        return new SourceView(
            this.id,
            this.name,
            this.url,
            this.settings,
            this.createdAt,
            this.lastPostDate,
            this.isLocked == 1,
            this.lastSuccessAt,
            this.lastErrorAt,
            this.lastSuccessCount,
            this.lastErrorsCount,
            this.isEnabled == 1);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getSettings() {
        return this.settings;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getLastPostDate() {
        return this.lastPostDate;
    }

    public Integer getIsLocked() {
        return this.isLocked;
    }

    public Set<Post> getPosts() {
        return this.posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public void setIsLocked(Integer isLocked) {
        this.isLocked = isLocked;
    }

    public Date getLastSuccessAt() {
        return lastSuccessAt;
    }

    public void setLastSuccessAt(Date lastSuccessAt) {
        this.lastSuccessAt = lastSuccessAt;
    }

    public Date getLastErrorAt() {
        return lastErrorAt;
    }

    public void setLastErrorAt(Date lastErrorAt) {
        this.lastErrorAt = lastErrorAt;
    }

    public Integer getLastSuccessCount() {
        return lastSuccessCount;
    }

    public void setLastSuccessCount(Integer lastSuccessCount) {
        this.lastSuccessCount = lastSuccessCount;
    }

    public Integer getLastErrorsCount() {
        return lastErrorsCount;
    }

    public void setLastErrorsCount(Integer lastErrorsCount) {
        this.lastErrorsCount = lastErrorsCount;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }
}
