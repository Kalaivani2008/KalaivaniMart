package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Product;
import java.util.List;

public interface CartDAO {

    void addToCart(long userId, long productId) throws Exception;

    List<Product> getCartProducts(long userId) throws Exception;

    void removeFromCart(long userId, long productId) throws Exception;
}