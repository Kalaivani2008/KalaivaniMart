package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Product;
import java.util.List;

public interface ProductDAO {

    List<Product> findAll() throws Exception;

    List<Product> searchProducts(
            String keyword,
            String category
    ) throws Exception;

    void addProduct(Product product) throws Exception;

    void updateProduct(Product product) throws Exception;

    void deleteProduct(long id) throws Exception;
}