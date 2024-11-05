package io.haedoang.step1.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static io.haedoang.step1.base.entity.CustomAuditorAware.DEFAULT_AUDITOR;

@Getter
@Setter
@EntityListeners(CustomAuditorAware.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "DELETED")
    protected Boolean deleted = Boolean.FALSE;

    @Column(name = "CREATED_BY")
    @CreatedBy
    protected String createdBy;

    @Column(name = "CREATED_AT", updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    protected String updatedBy;

    @Column(name = "UPDATED_AT")
    @LastModifiedDate
    protected LocalDateTime updatedAt = LocalDateTime.now();

    protected void deleted() {
        this.deleted = Boolean.TRUE;
    }

    @PrePersist
    public void prePersist() {
        if (!StringUtils.hasLength(this.createdBy)) {
            this.createdBy = DEFAULT_AUDITOR;
        }

        if (!StringUtils.hasLength(this.updatedBy)) {
            this.updatedBy = DEFAULT_AUDITOR;
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (!StringUtils.hasLength(this.updatedBy)) {
            this.updatedBy = DEFAULT_AUDITOR;
        }
    }
}
