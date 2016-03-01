
package com.rcs.shoe.shop.db;

import com.rcs.shoe.shop.common.exceptions.ShoeShopException;
import com.rcs.shoe.shop.db.config.ShoeShopDBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = ShoeShopDBConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LiquibaseExecutorTest {
    
    @Autowired
    LiquibaseExecutor liquibaseExecutor;
    
    @Test
    public void testUpdate() throws ShoeShopException {
        liquibaseExecutor.update();
    }
    
}
