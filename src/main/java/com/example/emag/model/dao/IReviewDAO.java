package com.example.emag.model.dao;

import com.example.emag.model.pojo.Review;
import java.sql.SQLException;
import java.util.List;

public interface IReviewDAO {

    void addReview(Review review) throws SQLException;
    List<Review> getAllReviewsForProduct(long productId) throws SQLException;
    List<Review> getAllReviewsForUser(long userId) throws SQLException;
}
