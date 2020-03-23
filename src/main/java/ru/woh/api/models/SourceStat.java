package ru.woh.api.models;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity(name = "SourceStat")
@Table(name = "source_stat")
public class SourceStat implements Serializable {
    @Embeddable
    public static class SourceStatPK implements Serializable {
        @Column(name = "source_id", nullable = false)
        private Long sourceId;

        @Column(name = "started_at", nullable = false)
        private Date startedAt;

        public SourceStatPK() {
        }

        public SourceStatPK(Long sourceId, Date startedAt) {
            this.sourceId = sourceId;
            this.startedAt = startedAt;
        }

        public Long getSourceId() {
            return sourceId;
        }

        public void setSourceId(Long sourceId) {
            this.sourceId = sourceId;
        }

        public Date getStartedAt() {
            return startedAt;
        }

        public void setStartedAt(Date startedAt) {
            this.startedAt = startedAt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SourceStatPK that = (SourceStatPK) o;
            return getSourceId().equals(that.getSourceId()) &&
                getStartedAt().equals(that.getStartedAt());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSourceId(), getStartedAt());
        }
    }

    @EmbeddedId
    @Column(unique = true)
    private SourceStatPK pk;

    @ManyToOne
    @JoinColumn(name = "source_id", insertable = false, updatable = false)
    private Source source;

    @Column(name = "started_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date startedAt;

    @Column(name = "finished_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date finishedAt;

    @Column(name = "parsed_count")
    private Integer parsedCount;

    @Column(name = "is_success")
    private Integer isSuccess;

    @Column(name = "error_text")
    @Type(type = "text")
    private String errorText;

    public SourceStat() {
    }

    public SourceStatPK getPk() {
        return pk;
    }

    public void setPk(SourceStatPK pk) {
        this.pk = pk;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getParsedCount() {
        return parsedCount;
    }

    public void setParsedCount(Integer parsedCount) {
        this.parsedCount = parsedCount;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SourceStat that = (SourceStat) o;
        return getSource().equals(that.getSource()) &&
            getStartedAt().equals(that.getStartedAt()) &&
            getFinishedAt().equals(that.getFinishedAt()) &&
            Objects.equals(getParsedCount(), that.getParsedCount()) &&
            getIsSuccess().equals(that.getIsSuccess()) &&
            Objects.equals(getErrorText(), that.getErrorText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSource(),
            getStartedAt(),
            getFinishedAt(),
            getParsedCount(),
            getIsSuccess(),
            getErrorText());
    }
}
