package com.rcs.shoe.shop.core.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractProduct extends AbstractAudit {

    @Column(name = "product_code", nullable = false)
    protected String productCode;

    @Column(name = "product_num", nullable = false)
    protected Integer productNum;

    @Column(name = "product_desc", nullable = true)
    protected String productDesc;

    @Column(name = "active", nullable = false)
    protected Boolean active;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getProductNum() {
        return productNum;
    }

    public void setProductNum(Integer productNum) {
        this.productNum = productNum;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
