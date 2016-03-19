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
    private List<ProductHistory> productHistory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductHistory> getProductHistory() {
        return productHistory;
    }

    public void setProductHistory(List<ProductHistory> productQuantityHistory) {
        this.productHistory = productQuantityHistory;
    }
}
