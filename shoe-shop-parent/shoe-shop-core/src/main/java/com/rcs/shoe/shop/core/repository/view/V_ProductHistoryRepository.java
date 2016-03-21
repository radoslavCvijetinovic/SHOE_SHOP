
package com.rcs.shoe.shop.core.repository.view;

import com.rcs.shoe.shop.core.entity.impl.view.V_ProductHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface V_ProductHistoryRepository extends CrudRepository<V_ProductHistory, Long>  {

    public List<V_ProductHistory> findByProductNum(Integer productNum);
    
}
