package ru.woh.api.views.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.woh.api.models.Post;
import ru.woh.api.models.Source;
import ru.woh.api.views.site.PostView;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SourceView {
    public SourceView() {
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

    public SourceSettings getSettings() {
        return this.settings;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public Date getLastPostDate() {
        return this.lastPostDate;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public List<PostView> getPosts() {
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

    public void setSettings(SourceSettings settings) {
        this.settings = settings;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setPosts(List<PostView> posts) {
        this.posts = posts;
    }

    public static class SourceSettings {
        private String url;
        private String linksSelector;
        private String descriptionSelector;
        private String titlesSelector;
        private String titleSelector;
        private String datesSelector;
        private String dateFormat;
        private String nextSelector;
        private String contentSelector;
        private String nextContentSelector;
        private String previewSelector;
        private Boolean isApproved;
        private Integer type;

        public SourceSettings() {
        }

        static SourceSettings fromJson(String json) {
            ObjectMapper mapper = new ObjectMapper();

            SourceSettings sourceSettings;
            try {
                sourceSettings = mapper.readValue(json, SourceSettings.class);
            } catch (IOException e) {
                sourceSettings = new SourceSettings();
            }

            return sourceSettings;
        }

        public String toString() {
            ObjectMapper mapper = new ObjectMapper();
            String result;

            try {
                result = mapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                result = "";
            }

            return result;
        }

        public String getUrl() {
            return this.url;
        }

        public String getLinksSelector() {
            return this.linksSelector;
        }

        public String getDescriptionSelector() {
            return this.descriptionSelector;
        }

        public String getTitlesSelector() {
            return this.titlesSelector;
        }

        public String getTitleSelector() {
            return this.titleSelector;
        }

        public String getDatesSelector() {
            return this.datesSelector;
        }

        public String getDateFormat() {
            return this.dateFormat;
        }

        public String getNextSelector() {
            return this.nextSelector;
        }

        public String getContentSelector() {
            return this.contentSelector;
        }

        public String getNextContentSelector() {
            return this.nextContentSelector;
        }

        public String getPreviewSelector() {
            return this.previewSelector;
        }

        public Boolean getIsApproved() {
            return this.isApproved;
        }

        public Integer getType() {
            return this.type;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setLinksSelector(String linksSelector) {
            this.linksSelector = linksSelector;
        }

        public void setDescriptionSelector(String descriptionSelector) {
            this.descriptionSelector = descriptionSelector;
        }

        public void setTitlesSelector(String titlesSelector) {
            this.titlesSelector = titlesSelector;
        }

        public void setTitleSelector(String titleSelector) {
            this.titleSelector = titleSelector;
        }

        public void setDatesSelector(String datesSelector) {
            this.datesSelector = datesSelector;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public void setNextSelector(String nextSelector) {
            this.nextSelector = nextSelector;
        }

        public void setContentSelector(String contentSelector) {
            this.contentSelector = contentSelector;
        }

        public void setNextContentSelector(String nextContentSelector) {
            this.nextContentSelector = nextContentSelector;
        }

        public void setPreviewSelector(String previewSelector) {
            this.previewSelector = previewSelector;
        }

        public void setIsApproved(Boolean isApproved) {
            this.isApproved = isApproved;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    private Long id;
    private String name;
    private String url;
    private SourceSettings settings;
    private Date createdAt;
    private Date lastPostDate;
    private Boolean isLocked;
    private List<PostView> posts;

    public SourceView(
        Long id,
        String name,
        String url,
        String settings,
        Date createdAt,
        Date lastPostDate,
        Boolean isLocked,
        Set<Post> posts
    ) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.settings = SourceSettings.fromJson(settings);
        this.createdAt = createdAt;
        this.lastPostDate = lastPostDate;
        this.isLocked = isLocked;
        this.posts = posts != null
            ? posts.stream().map(Post::view).collect(Collectors.toList())
            : null;
    }

    public Source model() {
        Source source = new Source();

        source.setId(this.id);
        source.setName(this.name);
        source.setUrl(this.url);
        source.setCreatedAt(this.createdAt);
        source.setLastPostDate(this.lastPostDate);
        source.setIsLocked((this.isLocked == null || this.isLocked == true) ? 1 : 0);
        source.setSettings(this.settings != null ? this.settings.toString() : "");

        return source;
    }
}
