package com.rcs.shoe.shop.core.entity;

import org.springframework.security.core.userdetails.User;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@MappedSuperclass
public abstract class AbstractAudit {

    @Column(name = "creation_time", nullable = false)
    protected Date creationTime;

    @Column(name = "modification_time", nullable = true)
    protected Date modificationTime;

    @Column(name = "created_by_user", nullable = false)
    protected String createdBy;

    @Column(name = "modified_by_user", nullable = true)
    protected String modifiedBy;

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @PrePersist
    public void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        this.creationTime = Date.from(now.toInstant());
        this.modificationTime = Date.from(now.toInstant());
        String username = getUsernameOfAuthenticatedUser();
        if(StringUtils.isNotBlank(username)) {
            this.createdBy = username;
        }
    }

    @PreUpdate
    public void preUpdate() {
        ZonedDateTime now = ZonedDateTime.now();
        this.modificationTime = Date.from(now.toInstant());
        String username = getUsernameOfAuthenticatedUser();
        if(StringUtils.isNotBlank(username)) {
            this.modifiedBy = username;
        }
    }

    private String getUsernameOfAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return ((User) authentication.getPrincipal()).getUsername();
    }
}
