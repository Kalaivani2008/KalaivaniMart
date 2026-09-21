package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Order;
import java.util.List;

public interface OrderDAO {

    Order createOrderFromCart(long buyerId) throws Exception;

    List<Order> getOrdersByBuyer(long buyerId) throws Exception;

    List<Order> getAllOrders() throws Exception;

    void updateOrderStatus(long orderId, String status)
            throws Exception;
}