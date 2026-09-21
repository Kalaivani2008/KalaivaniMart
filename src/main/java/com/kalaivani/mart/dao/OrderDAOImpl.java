package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {

    private final DataSource dataSource;

    public OrderDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Order createOrderFromCart(long buyerId)
            throws Exception {

        String cartSql =
                "SELECT c.product_id, c.quantity, " +
                "p.price, p.stock_qty " +
                "FROM cart_items c " +
                "JOIN products p ON c.product_id = p.id " +
                "WHERE c.user_id = ?";

        String orderSql =
                "INSERT INTO orders " +
                "(buyer_id, status, total_amount) " +
                "VALUES (?, ?, ?)";

        String itemSql =
                "INSERT INTO order_items " +
                "(order_id, product_id, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        String stockSql =
                "UPDATE products " +
                "SET stock_qty = stock_qty - ? " +
                "WHERE id = ? AND stock_qty >= ?";

        String clearCartSql =
                "DELETE FROM cart_items WHERE user_id = ?";

        try (Connection connection =
                     dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try {

                double totalAmount = 0;

                List<Long> productIds =
                        new ArrayList<>();

                List<Integer> quantities =
                        new ArrayList<>();

                List<Double> prices =
                        new ArrayList<>();


                // =========================
                // READ CART + CHECK STOCK
                // =========================

                try (PreparedStatement ps =
                             connection.prepareStatement(cartSql)) {

                    ps.setLong(1, buyerId);

                    try (ResultSet rs =
                                 ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new Exception("Cart is empty");
                        }

                        do {

                            long productId =
                                    rs.getLong("product_id");

                            int quantity =
                                    rs.getInt("quantity");

                            double price =
                                    rs.getDouble("price");

                            int stockQty =
                                    rs.getInt("stock_qty");


                            // Stock check
                            if (quantity > stockQty) {

                                throw new Exception(
                                        "Insufficient stock for product ID: "
                                                + productId
                                );
                            }


                            totalAmount +=
                                    price * quantity;

                            productIds.add(productId);
                            quantities.add(quantity);
                            prices.add(price);

                        } while (rs.next());
                    }
                }


                // =========================
                // CREATE ORDER
                // =========================

                long orderId;

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     orderSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    ps.setLong(1, buyerId);
                    ps.setString(2, "CONFIRMED");
                    ps.setDouble(3, totalAmount);

                    ps.executeUpdate();

                    try (ResultSet keys =
                                 ps.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new Exception(
                                    "Order ID not generated"
                            );
                        }

                        orderId =
                                keys.getLong(1);
                    }
                }


                // =========================
                // CREATE ORDER ITEMS
                // =========================

                try (PreparedStatement ps =
                             connection.prepareStatement(itemSql)) {

                    for (int i = 0;
                         i < productIds.size();
                         i++) {

                        ps.setLong(
                                1,
                                orderId
                        );

                        ps.setLong(
                                2,
                                productIds.get(i)
                        );

                        ps.setInt(
                                3,
                                quantities.get(i)
                        );

                        ps.setDouble(
                                4,
                                prices.get(i)
                        );

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }


                // =========================
                // REDUCE STOCK
                // =========================

                try (PreparedStatement ps =
                             connection.prepareStatement(stockSql)) {

                    for (int i = 0;
                         i < productIds.size();
                         i++) {

                        ps.setInt(
                                1,
                                quantities.get(i)
                        );

                        ps.setLong(
                                2,
                                productIds.get(i)
                        );

                        ps.setInt(
                                3,
                                quantities.get(i)
                        );

                        int updated =
                                ps.executeUpdate();

                        if (updated != 1) {

                            throw new Exception(
                                    "Stock update failed for product ID: "
                                            + productIds.get(i)
                            );
                        }
                    }
                }


                // =========================
                // CLEAR CART
                // =========================

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     clearCartSql)) {

                    ps.setLong(1, buyerId);

                    ps.executeUpdate();
                }


                // =========================
                // COMMIT
                // =========================

                connection.commit();


                return new Order(
                        orderId,
                        buyerId,
                        "CONFIRMED",
                        totalAmount
                );


            } catch (Exception e) {

                connection.rollback();

                throw e;
            }
        }
    }


    @Override
    public List<Order> getOrdersByBuyer(
            long buyerId)
            throws Exception {

        List<Order> orders =
                new ArrayList<>();

        String sql =
                "SELECT id, buyer_id, status, total_amount " +
                "FROM orders " +
                "WHERE buyer_id = ? " +
                "ORDER BY created_at DESC";

        try (Connection connection =
                     dataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setLong(1, buyerId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    Order order =
                            new Order();

                    order.setId(
                            rs.getLong("id")
                    );

                    order.setBuyerId(
                            rs.getLong("buyer_id")
                    );

                    order.setStatus(
                            rs.getString("status")
                    );

                    order.setTotalAmount(
                            rs.getDouble("total_amount")
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }


    @Override
    public List<Order> getAllOrders()
            throws Exception {

        List<Order> orders =
                new ArrayList<>();

        String sql =
                "SELECT id, buyer_id, status, total_amount " +
                "FROM orders " +
                "ORDER BY created_at DESC";

        try (Connection connection =
                     dataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Order order =
                        new Order();

                order.setId(
                        rs.getLong("id")
                );

                order.setBuyerId(
                        rs.getLong("buyer_id")
                );

                order.setStatus(
                        rs.getString("status")
                );

                order.setTotalAmount(
                        rs.getDouble("total_amount")
                );

                orders.add(order);
            }
        }

        return orders;
    }


    @Override
    public void updateOrderStatus(
            long orderId,
            String status)
            throws Exception {

        String sql =
                "UPDATE orders " +
                "SET status = ? " +
                "WHERE id = ?";

        try (Connection connection =
                     dataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, orderId);

            ps.executeUpdate();
        }
    }
}