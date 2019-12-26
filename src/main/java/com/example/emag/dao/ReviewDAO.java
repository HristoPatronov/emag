package com.example.emag.dao;

import com.example.emag.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements IReviewDAO {


    private static ReviewDAO mInstance;

    private ReviewDAO() {
    }

    public static ReviewDAO getInstance() {
        if (mInstance == null) {
            mInstance = new ReviewDAO();
        }
        return mInstance;
    }

    @Override
    public void addReview(Review review) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO reviews (title, text, rating, date, user_id, product_id) VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, review.getTitle());
            statement.setString(2, review.getText());
            statement.setInt(3, review.getRating());
            statement.setDate(4, Date.valueOf(review.getDate()));
            statement.setInt(5, review.getUserId());
            statement.setInt(6, review.getProductId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<Review> getAllReviewsForProduct(Integer productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM reviews WHERE id = ?;";
        Review review = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, productId);
            ResultSet set = statement.executeQuery();
            set.next();
            review = new Review(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getInt(4),
                    set.getDate(5).toLocalDate(),
                    set.getInt(6),
                    set.getInt(7));
            reviews.add(review);
        }
        return reviews;
    }

    @Override
    public List<Review> getAllReviewsForUser(Integer userId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM reviews WHERE id = ?;";
        Review review = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, userId);
            ResultSet set = statement.executeQuery();
            set.next();
            review = new Review(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getInt(4),
                    set.getDate(5).toLocalDate(),
                    set.getInt(6),
                    set.getInt(7));
            reviews.add(review);
        }
        return reviews;
    }
}
