/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository;

import com.rcs.shoe.shop.core.entity.ProductSizes;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface ProductSizesRepository extends CrudRepository<ProductSizes, Long> {
    
    public List<ProductSizes> findByProductCode(String productCode);

}
