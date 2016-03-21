/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository;

import com.rcs.shoe.shop.core.entity.impl.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

    public List<Product> findByProductCode(String productCode);
    
    public Product findByProductCodeAndProductNum(String productCode, Integer productNum);

    public Product findByProductNum(Integer productNum);

}
