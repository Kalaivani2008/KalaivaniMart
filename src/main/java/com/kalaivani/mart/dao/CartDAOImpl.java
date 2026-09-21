package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {

    private final DataSource dataSource;

    public CartDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addToCart(long userId, long productId) throws Exception {

        String checkSql =
                "SELECT quantity FROM cart_items " +
                "WHERE user_id = ? AND product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement check =
                     connection.prepareStatement(checkSql)) {

            check.setLong(1, userId);
            check.setLong(2, productId);

            try (ResultSet rs = check.executeQuery()) {

                if (rs.next()) {

                    String updateSql =
                            "UPDATE cart_items " +
                            "SET quantity = quantity + 1 " +
                            "WHERE user_id = ? AND product_id = ?";

                    try (PreparedStatement update =
                                 connection.prepareStatement(updateSql)) {

                        update.setLong(1, userId);
                        update.setLong(2, productId);
                        update.executeUpdate();
                    }

                } else {

                    String insertSql =
                            "INSERT INTO cart_items " +
                            "(user_id, product_id, quantity) " +
                            "VALUES (?, ?, 1)";

                    try (PreparedStatement insert =
                                 connection.prepareStatement(insertSql)) {

                        insert.setLong(1, userId);
                        insert.setLong(2, productId);
                        insert.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public List<Product> getCartProducts(long userId) throws Exception {

        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT p.id, p.seller_id, p.name,
                       p.description, p.price,
                       p.stock_qty, p.category
                FROM cart_items c
                JOIN products p ON c.product_id = p.id
                WHERE c.user_id = ?
                ORDER BY p.id
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Product product = new Product(
                            rs.getLong("id"),
                            rs.getLong("seller_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("stock_qty"),
                            rs.getString("category")
                    );

                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    public void removeFromCart(
            long userId,
            long productId
    ) throws Exception {

        String sql =
                "DELETE FROM cart_items " +
                "WHERE user_id = ? AND product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, productId);

            statement.executeUpdate();
        }
    }
}