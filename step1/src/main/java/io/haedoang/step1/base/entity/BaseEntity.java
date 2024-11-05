package io.haedoang.step1.base.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "DELETED")
    protected Boolean deleted = Boolean.FALSE;

    @Column(name = "CREATED_BY")
    @CreatedBy
    protected String createdBy;

    @Column(name = "CREATED_AT", updatable = false)
    @CreatedDate
    protected LocalDateTime createdAt;

    @Column(name = "UPDATED_BY")
    @LastModifiedBy
    protected String updatedBy;

    @Column(name = "UPDATED_AT")
    @LastModifiedDate
    protected LocalDateTime updatedAt;

    protected void deleted() {
        this.deleted = Boolean.TRUE;
    }
}
