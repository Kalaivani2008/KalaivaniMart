package com.kalaivani.mart.dao;

import com.kalaivani.mart.model.Review;

import java.util.List;

public interface ReviewDAO {

    void addReview(
            long productId,
            long userId,
            int rating,
            String comment
    ) throws Exception;

    List<Review> getReviewsByProduct(
            long productId
    ) throws Exception;
}