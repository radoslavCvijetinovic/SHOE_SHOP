package com.rcs.shoe.shop.core.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.rcs.shoe.shop.core.config.CoreConfig;
import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductSizes;
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

    @Autowired
    private ProductSizesRepository productQuantitiesRepository;

    @Test
    public void testFindAll() {
        List<Product> products = (List<Product>) productRepository.findAll();
        assertEquals(2, products.size());
        assertEquals("leo.messi@test.com", products.get(1).getCreatedBy());
    }

    @Test
    public void testFindQuantitiesForProduct() {
        List<Product> products = (List<Product>) productRepository.findAll();
        Product product = productRepository.findByProductCode(products.get(1).getProductCode());

        List<ProductSizes> quantities
                = (List<ProductSizes>) productQuantitiesRepository.findByProductCode(product.getProductCode());

        assertEquals(2, quantities.size());
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

        ProductSizes pq = new ProductSizes();
        pq.setCreatedBy("leo.messi@test.com");
        pq.setProductCode("V123456");
        pq.setSize(41);

        ProductSizes pq1 = new ProductSizes();
        pq1.setCreatedBy("leo.messi@test.com");
        pq1.setProductCode("V123456");
        pq1.setSize(45);

        productQuantitiesRepository.save(pq);
        productQuantitiesRepository.save(pq1);

        List<ProductSizes> sizes
                = (List<ProductSizes>) productQuantitiesRepository.findByProductCode(pq.getProductCode());

        assertEquals(2, sizes.size());
    }
}
