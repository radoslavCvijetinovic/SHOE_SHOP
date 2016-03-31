package com.rcs.shoe.shop.core.service;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
import com.rcs.shoe.shop.core.repository.ProductHistoryRepository;
import com.rcs.shoe.shop.core.repository.ProductRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductHistoryRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductsRepository;
import java.util.ArrayList;
import java.util.Calendar;
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

    public List<Product> findByProductCode(String prodCode) {
        return productRepository.findByProductCode(prodCode);
    }

    public List<V_ProductHistory> getProductQuantities(Integer productNumber) {
        List<V_ProductHistory> result = v_ProductHistoryRepository.findByProductNum(productNumber);
        return result;
    }

    public Map<Integer, V_ProductHistory> getProductQuantitiesMap(Integer productNumber) {
        List<V_ProductHistory> list = v_ProductHistoryRepository.findByProductNum(productNumber);
        Map<Integer, V_ProductHistory> result = new HashMap();
        for (V_ProductHistory ps : list) {
            result.put(ps.getSize(), ps);
        }
        return result;
    }

    @Transactional
    public ProductHistory saveProductQuantityHistory(ProductHistory quantityHistory) {
        return productHistoryRepository.save(quantityHistory);
    }

    public Product findByProductCodeAndProductNum(String productCode, Integer productNumber) {
        return productRepository.findByProductCodeAndProductNum(productCode, productNumber);
    }

    public Product findByProductNum(Integer productNum) {
        return productRepository.findByProductNum(productNum);
    }

    public V_Products findProductDetailsByProductNum(Integer productNum) {
        return v_ProductsRepository.findByProductNum(productNum);
    }

    public List<ProductHistory> getSalesHistoryByDate(Date from, Date to) {
        return productHistoryRepository.findByCreationTimeBetweenAndTypeOrderByCreationTimeDesc(from, to, 3);
    }

    public ProductHistory findHistoryById(Long id) {
        return productHistoryRepository.findOne(id);
    }

    @Transactional
    public void updateProductHistory(ProductHistory sale) {
        productHistoryRepository.save(sale);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.delete(id);
    }

    @Transactional
    public void optimizeProductHistory() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1); // minus year
        c.add(Calendar.MONTH, -6); // minus 6 months
        Date past = c.getTime();

        Map<String, ProductHistory> totals = new HashMap<String, ProductHistory>();
        List<ProductHistory> list = productHistoryRepository.findTop100ByModificationTimeLessThanAndTypeNot(past, 5);
        for (ProductHistory ph : list) {
            String key = getKey(ph);
            ProductHistory total = totals.get(key);

            if (total == null) {
                total = productHistoryRepository.findByProductNumAndProductCodeAndSizeAndType(
                        ph.getProductNum(), ph.getProductCode(), ph.getSize(), 5);
            }

            if (total == null) {
                total = new ProductHistory();
                total.setProductCode(ph.getProductCode());
                total.setProductNum(ph.getProductNum());
                total.setCreatedBy("rajkofon2");
                total.setSize(ph.getSize());
                total.setType(5);
                total.setQuantity(ph.getQuantity());
            } else {
                total.setQuantity(total.getQuantity() + ph.getQuantity());
            }
            totals.put(getKey(ph), total);

            productHistoryRepository.delete(ph);
        }

        if (!totals.isEmpty()) {
            productHistoryRepository.save(totals.values());
        }

        List<ProductHistory> zeros = productHistoryRepository.findByTypeAndQuantity(5, 0);
        if (!zeros.isEmpty()) {
            productHistoryRepository.delete(zeros);
        }
    }

    private String getKey(ProductHistory ph) {
        return ph.getProductNum() + "_" + ph.getProductCode() + "_" + ph.getSize();
    }

}
