package com.rcs.shoe.shop.core.service;

import com.rcs.shoe.shop.core.entity.impl.Product;
import com.rcs.shoe.shop.core.entity.impl.ProductQuantityHistory;
import com.rcs.shoe.shop.core.entity.impl.ProductSizes;
import com.rcs.shoe.shop.core.entity.impl.view.V_ProductQuantityHistory;
import com.rcs.shoe.shop.core.entity.impl.view.V_Products;
import com.rcs.shoe.shop.core.repository.ProductQuantityHistoryRepository;
import com.rcs.shoe.shop.core.repository.ProductSizesRepository;
import com.rcs.shoe.shop.core.repository.ProductRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductQuantityHistoryRepository;
import com.rcs.shoe.shop.core.repository.view.V_ProductsRepository;
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
    private ProductSizesRepository productSizesRepository;

    @Autowired
    private ProductQuantityHistoryRepository quantityRepository;

    @Autowired
    private V_ProductQuantityHistoryRepository vQuantityRepository;

    @Transactional
    public Product saveOrUpdateProduct(Product product) {
        List<ProductSizes> sizes = product.getProductSizes();
        List<ProductQuantityHistory> quantities = product.getProductQuantityHistory();

        Product productStored = productRepository.save(product);

        List<ProductSizes> sizesStored = (List<ProductSizes>) productSizesRepository.save(sizes);
        productStored.setProductSizes(sizesStored);

        List<ProductQuantityHistory> quantitiesStored = (List<ProductQuantityHistory>) quantityRepository.save(quantities);
        productStored.setProductQuantityHistory(quantitiesStored);

        return productStored;
    }

    public List<V_Products> findAll() {
        List<V_Products> products = (List<V_Products>) v_ProductsRepository.findAll();
        return products;
    }

    public Product findByProductCode(String prodCode) {
        return productRepository.findByProductCode(prodCode);
    }

    public List<V_ProductQuantityHistory> getProductQuantities(String productCode) {
        List<V_ProductQuantityHistory> result = vQuantityRepository.findByProductCode(productCode);
        return result;
    }

    public Map<Integer, V_ProductQuantityHistory> getProductQuantitiesMap(String productCode) {
        List<V_ProductQuantityHistory> list = vQuantityRepository.findByProductCode(productCode);
        Map<Integer, V_ProductQuantityHistory> result = new HashMap();
        for (V_ProductQuantityHistory ps : list) {
            result.put(ps.getSize(), ps);
        }
        return result;
    }

    public Map<Integer, ProductSizes> getProductSizesByProdCode(String productCode) {
        List<ProductSizes> productSizeses = productSizesRepository.findByProductCode(productCode);
        Map<Integer, ProductSizes> result = new HashMap();
        for (ProductSizes ps : productSizeses) {
            result.put(ps.getSize(), ps);
        }
        return result;
    }

    public ProductQuantityHistory saveProductQuantityHistory(ProductQuantityHistory quantityHistory) {
        return quantityRepository.save(quantityHistory);
    }

    public Product findByProductCodeAndProductNum(String productCode, String productNumber) {
        return productRepository.findByProductCodeAndProductNum(productCode, Integer.parseInt(productNumber));
    }

    public List<Product> findByProductNum(String productNum) {
        return productRepository.findByProductNum(Integer.parseInt(productNum));
    }

}
