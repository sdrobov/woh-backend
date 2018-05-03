package ru.woh.api.models;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
abstract public class UndeletableEntity implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    protected Long Id;
    @Column(name = "deleted_at")
    protected Date deletedAt;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
