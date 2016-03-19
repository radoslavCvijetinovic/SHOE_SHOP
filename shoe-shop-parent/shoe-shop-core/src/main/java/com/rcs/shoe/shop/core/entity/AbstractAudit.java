package com.rcs.shoe.shop.core.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

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

}
