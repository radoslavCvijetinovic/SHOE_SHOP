/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rcs.shoe.shop.core.repository.view;

import com.rcs.shoe.shop.core.entity.impl.view.V_ProductQuantityHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rajko
 */
public interface V_ProductQuantityHistoryRepository extends CrudRepository<V_ProductQuantityHistory, Long>  {

    public List<V_ProductQuantityHistory> findByProductCode(String productCode);
    
}
