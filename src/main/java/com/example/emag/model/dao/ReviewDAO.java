package com.example.emag.model.dao;

import com.example.emag.model.pojo.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewDAO implements IReviewDAO {

    @Override
    public void addReview(Review review) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO reviews (title, text, rating, date, user_id, product_id) VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, review.getTitle());
            statement.setString(2, review.getText());
            statement.setInt(3, review.getRating());
            statement.setDate(4, Date.valueOf(review.getDate()));
            statement.setLong(5, review.getUser().getId());
            statement.setLong(6, review.getProduct().getId());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            review.setId(keys.getLong(1));
        }
    }

    @Override
    public List<Review> getAllReviewsForProduct(long productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT r.*, u.*, p.*, sc.*, c.* FROM reviews AS r " +
                "JOIN users AS u ON r.user_id = u.id " +
                "JOIN products AS p ON r.product_id = p.id " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id WHERE r.product_id = ?;";
        Review review = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, productId);
            ResultSet set = statement.executeQuery();
            while(set.next()) {
                review = new Review(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getInt(4),
                        set.getDate(5).toLocalDate(),
                        new User(set.getLong(8),
                                set.getString(9),
                                set.getString(10),
                                set.getString(11),
                                set.getString(12),
                                set.getBoolean(13),
                                set.getBoolean(14)),
                        new Product(set.getLong(15),
                                set.getString(16),
                                set.getString(17),
                                set.getDouble(18),
                                set.getInt(19),
                                set.getInt(20),
                                set.getInt(21),
                                new SubCategory(set.getLong(24),
                                        set.getString(25),
                                        set.getBoolean(26),
                                        new Category(set.getLong(28),
                                                set.getString(29))),
                                set.getBoolean(23)));
                reviews.add(review);
            }
        }
        return reviews;
    }

    @Override
    public List<Review> getAllReviewsForUser(long userId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT r.*, u.*, p.*, sc.*, c.* FROM reviews AS r " +
                "JOIN users AS u ON r.user_id = u.id " +
                "JOIN products AS p ON r.product_id = p.id " +
                "JOIN sub_categories AS sc ON p.sub_category_id = sc.id " +
                "JOIN categories AS c ON sc.category_id = c.id WHERE r.user_id = ?;";
        Review review = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                review = new Review(set.getLong(1),
                        set.getString(2),
                        set.getString(3),
                        set.getInt(4),
                        set.getDate(5).toLocalDate(),
                        new User(set.getLong(8),
                                set.getString(9),
                                set.getString(10),
                                set.getString(11),
                                set.getString(12),
                                set.getBoolean(13),
                                set.getBoolean(14)),
                        new Product(set.getLong(15),
                                set.getString(16),
                                set.getString(17),
                                set.getDouble(18),
                                set.getInt(19),
                                set.getInt(20),
                                set.getInt(21),
                                new SubCategory(set.getLong(24),
                                        set.getString(25),
                                        set.getBoolean(26),
                                        new Category(set.getLong(28),
                                                set.getString(29))),
                                set.getBoolean(23)));
                reviews.add(review);
            }
        }
        return reviews;
    }
}
