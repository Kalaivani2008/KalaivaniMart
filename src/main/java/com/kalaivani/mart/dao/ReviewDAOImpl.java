package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Review;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    private final DataSource dataSource;

    public ReviewDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addReview(
            long productId,
            long userId,
            int rating,
            String comment)
            throws Exception {

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException(
                    "Rating must be between 1 and 5"
            );
        }

        String sql =
                "INSERT INTO reviews " +
                "(product_id, user_id, rating, comment) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     dataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setLong(1, productId);
            ps.setLong(2, userId);
            ps.setInt(3, rating);
            ps.setString(4, comment);

            ps.executeUpdate();
        }
    }

    @Override
    public List<Review> getReviewsByProduct(
            long productId)
            throws Exception {

        List<Review> reviews =
                new ArrayList<>();

        String sql =
                "SELECT id, product_id, user_id, " +
                "rating, comment " +
                "FROM reviews " +
                "WHERE product_id = ? " +
                "ORDER BY created_at DESC";

        try (Connection connection =
                     dataSource.getConnection();

             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setLong(1, productId);

            try (ResultSet rs =
                         ps.executeQuery()) {

                while (rs.next()) {

                    Review review =
                            new Review();

                    review.setId(
                            rs.getLong("id")
                    );

                    review.setProductId(
                            rs.getLong("product_id")
                    );

                    review.setUserId(
                            rs.getLong("user_id")
                    );

                    review.setRating(
                            rs.getInt("rating")
                    );

                    review.setComment(
                            rs.getString("comment")
                    );

                    reviews.add(review);
                }
            }
        }

        return reviews;
    }
}