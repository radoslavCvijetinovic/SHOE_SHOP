package com.rcs.shoe.shop.core.service;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
import com.rcs.shoe.shop.core.repository.ProductHistoryRepository;
import com.rcs.shoe.shop.core.repository.ProductRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductHistoryRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductsRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private V_ProductsRepository v_ProductsRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Autowired
    private V_ProductHistoryRepository v_ProductHistoryRepository;

    @Transactional
    public Product saveOrUpdateProduct(Product product) {
        List<ProductHistory> quantities = product.getProductHistory();

        Product productStored = productRepository.save(product);

        List<ProductHistory> quantitiesStored = (List<ProductHistory>) productHistoryRepository.save(quantities);
        productStored.setProductHistory(quantitiesStored);

        return productStored;
    }

    public List<V_Products> findAll() {
        List<V_Products> products = (List<V_Products>) v_ProductsRepository.findAll();
        return products;
    }

    public Product findByProductCode(String prodCode) {
        return productRepository.findByProductCode(prodCode);
    }

    public List<V_ProductHistory> getProductQuantities(String productCode) {
        List<V_ProductHistory> result = v_ProductHistoryRepository.findByProductCode(productCode);
        return result;
    }

    public Map<Integer, V_ProductHistory> getProductQuantitiesMap(String productCode) {
        List<V_ProductHistory> list = v_ProductHistoryRepository.findByProductCode(productCode);
        Map<Integer, V_ProductHistory> result = new HashMap();
        for (V_ProductHistory ps : list) {
            result.put(ps.getSize(), ps);
        }
        return result;
    }

    public ProductHistory saveProductQuantityHistory(ProductHistory quantityHistory) {
        return productHistoryRepository.save(quantityHistory);
    }

    public Product findByProductCodeAndProductNum(String productCode, String productNumber) {
        return productRepository.findByProductCodeAndProductNum(productCode, Integer.parseInt(productNumber));
    }

    public List<Product> findByProductNum(String productNum) {
        return productRepository.findByProductNum(Integer.parseInt(productNum));
    }

    public List<ProductHistory> getSalesHistoryByDate(Date from, Date to) {
        return productHistoryRepository.findByCreationTimeBetweenAndTypeOrderByCreationTimeDesc(from, to, 3);
    }

    public ProductHistory findHistoryById(Long id) {
        return productHistoryRepository.findOne(id);
    }

    public void updateProductHistory(ProductHistory sale) {
        productHistoryRepository.save(sale);
    }

}
