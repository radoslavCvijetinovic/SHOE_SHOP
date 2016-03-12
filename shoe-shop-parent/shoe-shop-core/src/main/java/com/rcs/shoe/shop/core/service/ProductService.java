package com.rcs.shoe.shop.core.service;

import com.rcs.shoe.shop.core.entity.Product;
import com.rcs.shoe.shop.core.entity.ProductSizes;
import com.rcs.shoe.shop.core.entity.V_Products;
import com.rcs.shoe.shop.core.repository.ProductSizesRepository;
import com.rcs.shoe.shop.core.repository.ProductRepository;
import com.rcs.shoe.shop.core.repository.V_ProductsRepository;
import java.util.List;
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
    private ProductSizesRepository productQuantitiesRepository;

    @Transactional
    public Product saveProduct(Product product) {

        product = productRepository.save(product);

        if (product.getProductSizes() != null && !product.getProductSizes().isEmpty()) {
            List<ProductSizes> sizes
                    = (List<ProductSizes>) productQuantitiesRepository.save(product.getProductSizes());

            product.setProductSizes(sizes);
        }

        return product;
    }

    public List<V_Products> findAll() {
        List<V_Products> products = (List<V_Products>) v_ProductsRepository.findAll();
        return products;
    }

}
