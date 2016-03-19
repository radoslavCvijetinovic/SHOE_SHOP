package com.rcs.shoe.shop.core.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.rcs.shoe.shop.core.config.CoreConfig;
import com.rcs.shoe.shop.core.entity.impl.Product;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(value = {"classpath:dbunit/user-entries.xml", "classpath:dbunit/product-entries.xml"})
@DatabaseTearDown(value = {"classpath:dbunit/clear-user-entries.xml", "classpath:dbunit/clear-product-entries.xml"})
public class ITProductRepository {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        assertEquals(2, products.size());
        assertEquals("leo.messi@test.com", products.get(1).getCreatedBy());
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setActive(Boolean.TRUE);
        product.setCreatedBy("leo.messi@test.com");
        product.setModificationTime(null);
        product.setModifiedBy(null);
        product.setProductCode("V123456");
        product.setProductDesc("Fake product");
        product.setProductNum(003);

        product = productRepository.save(product);

        Assert.assertNotNull(product.getId());

    }
}
