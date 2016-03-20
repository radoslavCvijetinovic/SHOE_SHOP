/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository;

import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface ProductHistoryRepository extends CrudRepository<ProductHistory, Long>{
    
    List<ProductHistory> findByCreationTimeGreaterThanAndType(Date date, Integer type);
    
    List<ProductHistory> findByCreationTimeBetweenAndTypeOrderByCreationTimeDesc(
            Date from, Date to, Integer type);
}
