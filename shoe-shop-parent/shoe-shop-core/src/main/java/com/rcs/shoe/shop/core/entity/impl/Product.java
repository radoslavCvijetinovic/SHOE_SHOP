/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.entity.impl;

import com.rcs.shoe.shop.core.entity.AbstractProduct;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Rajko
 */
@Entity
@Table(name = "products")
public class Product extends AbstractProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Transient
    private List<ProductSizes> productSizes;

    @Transient
    private List<ProductQuantityHistory> productQuantityHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductSizes> getProductSizes() {
        return productSizes;
    }

    public void setProductSizes(List<ProductSizes> productSizes) {
        this.productSizes = productSizes;
    }

    public List<ProductQuantityHistory> getProductQuantityHistory() {
        return productQuantityHistory;
    }

    public void setProductQuantityHistory(List<ProductQuantityHistory> productQuantityHistory) {
        this.productQuantityHistory = productQuantityHistory;
    }
}
