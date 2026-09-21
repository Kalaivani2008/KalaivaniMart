package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Product;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    private final DataSource dataSource;

    public ProductDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Product> findAll() throws Exception {

        return searchProducts("", "");
    }

    @Override
    public List<Product> searchProducts(
            String keyword,
            String category
    ) throws Exception {

        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT id, seller_id, name, description,
                       price, stock_qty, category
                FROM products
                WHERE LOWER(name) LIKE ?
                  AND LOWER(category) LIKE ?
                ORDER BY id
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            String keywordValue =
                    keyword == null ? "" : keyword.trim().toLowerCase();

            String categoryValue =
                    category == null ? "" : category.trim().toLowerCase();

            statement.setString(1, "%" + keywordValue + "%");
            statement.setString(2, "%" + categoryValue + "%");

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Product product = new Product(
                            resultSet.getLong("id"),
                            resultSet.getLong("seller_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("stock_qty"),
                            resultSet.getString("category")
                    );

                    products.add(product);
                }
            }
        }

        return products;
    }

    @Override
    public void addProduct(Product product) throws Exception {

        String sql = """
                INSERT INTO products
                (seller_id, name, description, price, stock_qty, category)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, product.getSellerId());
            statement.setString(2, product.getName());
            statement.setString(3, product.getDescription());
            statement.setDouble(4, product.getPrice());
            statement.setInt(5, product.getStockQty());
            statement.setString(6, product.getCategory());

            statement.executeUpdate();
        }
    }

    @Override
    public void updateProduct(Product product) throws Exception {

        String sql = """
                UPDATE products
                SET name = ?,
                    description = ?,
                    price = ?,
                    stock_qty = ?,
                    category = ?
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setDouble(3, product.getPrice());
            statement.setInt(4, product.getStockQty());
            statement.setString(5, product.getCategory());
            statement.setLong(6, product.getId());

            statement.executeUpdate();
        }
    }

    @Override
    public void deleteProduct(long id) throws Exception {

        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            statement.executeUpdate();
        }
    }
}