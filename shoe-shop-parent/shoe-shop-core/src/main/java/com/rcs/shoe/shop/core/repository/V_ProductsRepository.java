/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository;

import com.rcs.shoe.shop.core.entity.V_Products;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface V_ProductsRepository extends CrudRepository<V_Products, Long> {

    public V_Products findByProductCode(String productCode);

}