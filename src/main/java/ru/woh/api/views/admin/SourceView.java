package ru.woh.api.views.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.woh.api.models.Source;

import java.io.IOException;
import java.util.Date;

public class SourceView {
    private Long id;
    private String name;
    private String url;
    private SourceSettings settings;
    private Date createdAt;
    private Date lastPostDate;
    private Boolean isLocked;
    private Date lastSuccessAt;
    private Date lastErrorAt;
    private Integer lastSuccessCount;
    private Integer lastErrorsCount;

    public SourceView() {
    }

    public SourceView(
        Long id,
        String name,
        String url,
        String settings,
        Date createdAt,
        Date lastPostDate,
        Boolean isLocked,
        Date lastSuccessAt, Date lastErrorAt, Integer lastSuccessCount, Integer lastErrorsCount) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.settings = SourceSettings.fromJson(settings);
        this.createdAt = createdAt;
        this.lastPostDate = lastPostDate;
        this.isLocked = isLocked;
        this.lastSuccessAt = lastSuccessAt;
        this.lastErrorAt = lastErrorAt;
        this.lastSuccessCount = lastSuccessCount;
        this.lastErrorsCount = lastErrorsCount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SourceSettings getSettings() {
        return this.settings;
    }

    public void setSettings(SourceSettings settings) {
        this.settings = settings;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastPostDate() {
        return this.lastPostDate;
    }

    public void setLastPostDate(Date lastPostDate) {
        this.lastPostDate = lastPostDate;
    }

    public Boolean getIsLocked() {
        return this.isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
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

    public Source model() {
        Source source = new Source();

        source.setId(this.id);
        source.setName(this.name);
        source.setUrl(this.url);
        source.setCreatedAt(this.createdAt);
        source.setLastPostDate(this.lastPostDate);
        source.setIsLocked(0);
        source.setSettings(this.settings != null ? this.settings.toString() : "");
        source.setLastSuccessAt(this.lastSuccessAt);
        source.setLastErrorAt(this.lastErrorAt);
        source.setLastSuccessCount(this.lastSuccessCount);
        source.setLastErrorsCount(this.lastErrorsCount);

        return source;
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

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLinksSelector() {
            return this.linksSelector;
        }

        public void setLinksSelector(String linksSelector) {
            this.linksSelector = linksSelector;
        }

        public String getDescriptionSelector() {
            return this.descriptionSelector;
        }

        public void setDescriptionSelector(String descriptionSelector) {
            this.descriptionSelector = descriptionSelector;
        }

        public String getTitlesSelector() {
            return this.titlesSelector;
        }

        public void setTitlesSelector(String titlesSelector) {
            this.titlesSelector = titlesSelector;
        }

        public String getTitleSelector() {
            return this.titleSelector;
        }

        public void setTitleSelector(String titleSelector) {
            this.titleSelector = titleSelector;
        }

        public String getDatesSelector() {
            return this.datesSelector;
        }

        public void setDatesSelector(String datesSelector) {
            this.datesSelector = datesSelector;
        }

        public String getDateFormat() {
            return this.dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

        public String getNextSelector() {
            return this.nextSelector;
        }

        public void setNextSelector(String nextSelector) {
            this.nextSelector = nextSelector;
        }

        public String getContentSelector() {
            return this.contentSelector;
        }

        public void setContentSelector(String contentSelector) {
            this.contentSelector = contentSelector;
        }

        public String getNextContentSelector() {
            return this.nextContentSelector;
        }

        public void setNextContentSelector(String nextContentSelector) {
            this.nextContentSelector = nextContentSelector;
        }

        public String getPreviewSelector() {
            return this.previewSelector;
        }

        public void setPreviewSelector(String previewSelector) {
            this.previewSelector = previewSelector;
        }

        public Boolean getIsApproved() {
            return this.isApproved;
        }

        public void setIsApproved(Boolean isApproved) {
            this.isApproved = isApproved;
        }

        public Integer getType() {
            return this.type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
